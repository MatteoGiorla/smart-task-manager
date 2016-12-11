package ch.epfl.sweng.project.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.project.R;

public class SettingsSuggestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_suggest);

        sendEmail();

    }

    /**
     * Construct and send the mail to nanchenbastian@gmail.com
     */
    private void sendEmail() {
        Button sendButton = (Button) findViewById(R.id.settings_suggest_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.settings_suggest_name);
                EditText mail = (EditText) findViewById(R.id.settings_suggest_mail);
                EditText message = (EditText) findViewById(R.id.settings_suggest_message);

                // Control if all the fields are filled and if the email address has a correct format
                if (name.getText().toString().trim().equals("")) {
                    name.setError("This field is mandatory");
                    return;
                } else if (mail.getText().toString().trim().equals("")) {
                    mail.setError("This field is mandatory");
                    return;
                } else if (!isEmailValid(mail.getText().toString())) {
                    mail.setError("This email address is not valid");
                    return;
                } else if (message.getText().toString().trim().equals("")) {
                    message.setError("This field is mandatory");
                    return;
                }

                // Create and send the mail
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"nanchenbastian@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, new String[] {"[Taskit] Suggest a Feedback"});
                intent.putExtra(Intent.EXTRA_TEXT, "Name: "+ name.getText()
                        +"\n" + "Email address: "+ mail.getText()
                        +"\n\n"+ message.getText());

                startActivity(Intent.createChooser(intent, "Suggest a feature:"));
            }
        });
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email text typed by the user
     * @return boolean true for valid false for invalid
     */
    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
