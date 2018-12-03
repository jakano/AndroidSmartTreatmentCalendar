package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import android.text.TextUtils;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailTextField;
    private EditText mPasswordTextField;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        mEmailTextField = findViewById(R.id.login_email);
        mPasswordTextField = findViewById(R.id.login_password);
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginTap();
            }
        });
        Database.initialize();

    }

    private void onLoginTap() {
        mEmailTextField = findViewById(R.id.login_email);
        mPasswordTextField = findViewById(R.id.login_password);
        String em_text = mEmailTextField.getText().toString();
        String ps_text = mPasswordTextField.getText().toString();
        View focusView = null;
        if (TextUtils.isEmpty(em_text)){
            mEmailTextField.setError("You must enter an email");
            focusView = mEmailTextField;
            focusView.requestFocus();
        }
        if (TextUtils.isEmpty(ps_text)) {
            mPasswordTextField.setError("You must enter a password");
            focusView = mPasswordTextField;
            focusView.requestFocus();
        } else {
            String email = mEmailTextField.getText().toString().trim();
            String password = mPasswordTextField.getText().toString().trim();


            Database.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(), "Authentication successful!",
                                        Toast.LENGTH_SHORT).show();
                                if (HomeScreenActivity.instance != null) {
                                    try {
                                        HomeScreenActivity.instance.finish();
                                    } catch (Exception e){
                                        Log.d("home_screen finish", e.getMessage());
                                    }
                                }
                                Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                                finish();
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast toast = Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_SHORT);
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    toast = Toast.makeText(getApplicationContext(), "Your credentials are invalid, please try again!", Toast.LENGTH_SHORT);
                                } catch (FirebaseAuthInvalidUserException e) {
                                    toast = Toast.makeText(getApplicationContext(), "This is not a valid user account.", Toast.LENGTH_SHORT);
                                } catch (FirebaseAuthUserCollisionException e) {
                                    toast = Toast.makeText(getApplicationContext(), "This email is already in use.", Toast.LENGTH_SHORT);
                                } catch (Exception e) {
                                    toast = Toast.makeText(getApplicationContext(), "Failed Authentication: " + e.getMessage(), Toast.LENGTH_SHORT);
                                }
                                toast.show();
                            }
                        }
                    });
        }
    }
}