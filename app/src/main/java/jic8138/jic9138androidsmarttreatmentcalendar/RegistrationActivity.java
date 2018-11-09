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
        //TODO: Database implementation
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
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}