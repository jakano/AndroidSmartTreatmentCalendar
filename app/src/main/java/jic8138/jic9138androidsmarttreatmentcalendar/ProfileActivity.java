package jic8138.jic9138androidsmarttreatmentcalendar;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

//        TextView email = findViewById(R.id.email_actual);
//        email.setText(currentUser.getEmail());
//
//        TextView name = findViewById(R.id.name_actual);
//        name.setText(currentUser.getUid());
    }
}
