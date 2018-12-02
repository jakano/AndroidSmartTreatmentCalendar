package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;
import android.text.TextUtils;

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
        Database.initialize();

    }

    private void onRegisterButtonTap() {
        mFirstNameTextField = findViewById(R.id.reg_first_name_textfield);
        mLastnameTextField = findViewById(R.id.reg_last_name_textfield);
        mEmailTextField = findViewById(R.id.reg_email_textfield);
        mGTIDTextField = findViewById(R.id.reg_gtid_textfield);
        mPasswordTextField = findViewById(R.id.reg_password_textfield);
        mConfirmPasswordTextField = findViewById(R.id.reg_confirm_password_textfield);

        String fn_text = mFirstNameTextField.getText().toString();
        String ln_text = mLastnameTextField.getText().toString();
        String em_text = mEmailTextField.getText().toString();
        String gtid_text = mGTIDTextField.getText().toString();
        String ps_text = mPasswordTextField.getText().toString();
        String cps_text = mConfirmPasswordTextField.getText().toString();

        Boolean validCredentials = true;

        View focusView = null;
        if (TextUtils.isEmpty(fn_text)){
            mFirstNameTextField.setError("You must enter a first name");
            focusView = mFirstNameTextField;
            focusView.requestFocus();
            validCredentials = false;
        }
        if (TextUtils.isEmpty(ln_text)) {
            mLastnameTextField.setError("You must enter a last name");
            focusView = mLastnameTextField;
            focusView.requestFocus();
            validCredentials = false;
        }
        if (TextUtils.isEmpty(em_text)){
            mEmailTextField.setError("You must enter an email");
            focusView = mEmailTextField;
            focusView.requestFocus();
            validCredentials = false;
        }
        if (TextUtils.isEmpty(gtid_text)) {
            mGTIDTextField.setError("You must enter a GTID");
            focusView = mGTIDTextField;
            focusView.requestFocus();
            validCredentials = false;
        } else if (gtid_text.length() != 9) {
            mGTIDTextField.setError("Valid GTID must be exactly 9 digits");
            focusView = mGTIDTextField;
            focusView.requestFocus();
            validCredentials = false;
        }
        if (TextUtils.isEmpty(cps_text)) {
            mConfirmPasswordTextField.setError("You must confirm password");
            focusView = mConfirmPasswordTextField;
            focusView.requestFocus();
            validCredentials = false;
        } else if (!ps_text.equals(cps_text)) {
            mPasswordTextField.setError("Passwords must match each other");
            focusView = mPasswordTextField;
            focusView.requestFocus();
            mConfirmPasswordTextField.setError("Passwords much match each other");
            focusView = mConfirmPasswordTextField;
            focusView.requestFocus();
            validCredentials = false;
        }
        if (TextUtils.isEmpty(ps_text)) {
            mPasswordTextField.setError("You must enter a password");
            focusView = mPasswordTextField;
            focusView.requestFocus();
            validCredentials = false;
        } else if (ps_text.trim().length() < 6) {
            mPasswordTextField.setError("Password must have atleast 6 characters");
            focusView = mPasswordTextField;
            focusView.requestFocus();
            validCredentials = false;
        }


        if (validCredentials) {
            String email = mEmailTextField.getText().toString().trim();
            String password = mPasswordTextField.getText().toString().trim();
            String gtid = mGTIDTextField.getText().toString().trim();
            String first = mFirstNameTextField.getText().toString().trim();
            String last = mLastnameTextField.getText().toString().trim();

            Database.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Registration", "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    Toast toast;


                    if (!task.isSuccessful()) {
                        toast = Toast.makeText(getApplicationContext(), "Unsuccessful registration ;_;", Toast.LENGTH_SHORT);
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            toast = Toast.makeText(getApplicationContext(), "Your password is too weak!", Toast.LENGTH_SHORT);
                        } catch (FirebaseAuthUserCollisionException e) {
                            toast = Toast.makeText(getApplicationContext(), "A user with this email address already exists!", Toast.LENGTH_SHORT);
                        } catch (Exception e) {
                            toast = Toast.makeText(getApplicationContext(), "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else {
                        toast = Toast.makeText(getApplicationContext(),
                                "Successfully registered account!",
                                Toast.LENGTH_SHORT);
                        toast.show();

                        FirebaseUser user = task.getResult().getUser();
                        DatabaseReference ref = Database.getReference("users");
                        User u = new User(gtid, email, first, last);
                        ref.child(user.getUid()).setValue(u.toMap());
                        // changes to Login page
                        Intent intent = new Intent(RegistrationActivity.this, HomeScreenActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }
    }
}