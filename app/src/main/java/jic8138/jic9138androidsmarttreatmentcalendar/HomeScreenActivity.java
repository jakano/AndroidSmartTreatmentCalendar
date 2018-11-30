package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    public static HomeScreenActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.home_screen);

        Button mLoginButton = findViewById(R.id.homescreen_login_button);
        Button mRegistrationButton = findViewById(R.id.homescreen_register_button);

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

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

    private void onLoginButtonTap() {
        Intent intent = new Intent(HomeScreenActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void onRegisterButtonTap() {
        Intent intent = new Intent(HomeScreenActivity.this, RegistrationActivity.class);
        startActivity(intent);

    }
}
