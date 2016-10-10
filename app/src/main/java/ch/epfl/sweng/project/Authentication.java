package ch.epfl.sweng.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created on 07.10.16.
 */

public class Authentication {
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_authentication); // créer activity_authentication
    }
}
