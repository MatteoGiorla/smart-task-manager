package ch.epfl.sweng.project.authentication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.project.R;

import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button mSignIn = (Button) findViewById(R.id.mSignIn);
        mSignIn.setEnabled(false);

        mUsername = (EditText) findViewById(R.id.mUsername);
        mPassword = (EditText) findViewById(R.id.mPassword);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);

    }
}