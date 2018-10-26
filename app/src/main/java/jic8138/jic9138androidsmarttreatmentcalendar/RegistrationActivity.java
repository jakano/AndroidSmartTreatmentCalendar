package jic8138.jic9138androidsmarttreatmentcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mFirstNameTextField;
    private EditText mLastnameTextField;
    private EditText mEmailTextField;
    private EditText mGTIDTextField;
    private EditText mPasswordTextField;
    private EditText mConfirmPasswordTextField;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);

        mFirstNameTextField = findViewById(R.id.reg_first_name_textfield);
        mLastnameTextField = findViewById(R.id.reg_last_name_textfield);
        mEmailTextField = findViewById(R.id.reg_email_textfield);
        mGTIDTextField = findViewById(R.id.reg_gtid_textfield);
        mPasswordTextField = findViewById(R.id.reg_password_textfield);
        mConfirmPasswordTextField = findViewById(R.id.reg_confirm_password_textfield);
        mRegisterButton = findViewById(R.id.reg_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonTap();
            }
        });

    }

    private void onRegisterButtonTap() {
        //TODO: Database implementation
        this.finish();
    }
}
