package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        mLoginButton = findViewById(R.id.homescreen_login_button);
        mRegistrationButton = findViewById(R.id.homescreen_register_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonTap();
            }
        });

        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonTap();
            }
        });
    }

    private void onLoginButtonTap() {
        Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void onRegisterButtonTap() {
        Intent inent = new Intent(HomeScreenActivity.this, RegistrationActivity.class);
        startActivity(inent);

    }
}
