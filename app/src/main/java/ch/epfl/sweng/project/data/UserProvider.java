
package ch.epfl.sweng.project.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Class that decide which provider the app use in
 * order to manipulate users in the database
 */
public class UserProvider {
    public static final String FIREBASE_PROVIDER = "Firebase";
    public static final String TEST_PROVIDER = "Tests";
    public static String mProvider = FIREBASE_PROVIDER;

    /**
     * Getter that return the Proxy to reach
     * the database
     * @return UserHelper, the proxy
     */
    public UserHelper getUserProvider() {
        switch (mProvider) {
            case FIREBASE_PROVIDER:
                return new FirebaseUserHelper();
            case TEST_PROVIDER:
                return new LocalUserHelper();
            default:
                throw new IllegalArgumentException("This provider does not exists !");
        }
    }

    public FirebaseUser getFirebaseAuthUser(){
        switch (mProvider){
            case FIREBASE_PROVIDER:
                return FirebaseAuth.getInstance().getCurrentUser();
            case TEST_PROVIDER:
                //for some reason the jenkins emulator doesn't allow to do this,
                //so little hack to make this work anyway.
                /*FirebaseAuth.getInstance().signInAnonymously();
                return FirebaseAuth.getInstance().getCurrentUser();*/
                throw new IllegalStateException("In state case");
            default:
                throw new IllegalArgumentException("This provider does not exists !");
        }
    }

    /**
     * Setter that allow to switch between Providers
     * @param provider should be UserProvider.FIREBASE_PROVIDER or
     *                 UserProvider.TEST_PROVIDER
     */
    public static void setProvider(String provider) {
        mProvider = provider;
    }
}