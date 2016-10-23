package ch.epfl.sweng.project.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.R;


/**
 * Class assuring the Login Activity and Authentication of the user
 */
public class LoginActivity
        extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener
{

    private GoogleApiClient mGoogleClient;
    private CallbackManager mFacebook;

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    /**
     * Override the onCreate method
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook SDK:
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        // Sign In Buttons:
        findViewById(R.id.google_sign_in_button).setOnClickListener(this);
        findViewById(R.id.facebook_sign_in_button).setOnClickListener(this);

        // configure Google Sign In:
        GoogleSignInOptions googleSignIn =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail() // to request the user email
                .build();

        mGoogleClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignIn)
                .build();

        // configure Firebase part:
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // configure Facebook Sign In:
        mFacebook = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(LoginActivity.this, "Authentication canceled.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Override the onStart method.
     * Attach the listener to the FirebaseAuth instance.
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Override the onStop method.
     * Remove the listener to the FirebaseAuth instance.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Override the onActivityResult method.
     * Retrieve the sign in result, use Firebase for the authentication (OAuth2) and
     * make the UI update in consequence.
     *
     * @param requestCode The integer request code supplied to startActivityForResult
     *                    used as an identifier.
     * @param resultCode  The integer result code returned by the child activity
     * @param data        An intent which can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Facebook Sign In:
        mFacebook.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase:
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed:
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Authentication with Google in using Firebase (OAuth2)
     *
     * @param acct Class that holds the basic account information of the signed in Google user.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            if (task.getException().getMessage().contains("An account already " +
                                    "exists with the same email address but different sign-in " +
                                    "credentials.")) {
                                Toast.makeText(LoginActivity.this, "You must use the same " +
                                                "authentication service as before.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    /**
     * After a user successfully signs in get an access token for the signed-in user,
     * exchange it for a Firebase credential, and authenticate with Firebase using
     * the Firebase credential.
     *
     * @param token access token delivered by Facebook
     */
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            if (task.getException().getMessage().contains("An account already " +
                                    "exists with the same email address but different sign-in " +
                                    "credentials.")) {
                                Toast.makeText(LoginActivity.this, "You must use the same " +
                                        "authentication service as before.",
                                        Toast.LENGTH_LONG).show();
                                LoginManager.getInstance().logOut();
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    /**
     * Override the onConnectionFailed method.
     *
     * @param connectionResult the connection result, indication on the failure.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Override the onClick method.
     *
     * @param v Required argument.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                signIn();
                break;
        }
    }

    /**
     * Override the signIn method.
     * Create a sign in intent.
     */
    public void signIn() {
        Intent signIn = Auth.GoogleSignInApi.getSignInIntent(mGoogleClient);
        // start the intent:
        startActivityForResult(signIn, RC_SIGN_IN);
    }

    /**
     * Not used method for the moment but maybe useful in the future.
     */
    /*private void signOut() {
        // Firebase sign out
        //mAuth.signOut();
        
        if(mGoogleClient.isConnected()) {
            // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //updateUI(null);
                    }
                });
        } else {
            // Facebook log out
            LoginManager.getInstance().logOut();
        }


        FirebaseAuth.getInstance().signOut();

    }*/
}