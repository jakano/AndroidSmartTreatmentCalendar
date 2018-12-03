package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String eventUser = user.getUid();

        DatabaseReference ref = Database.getReference("users").child(eventUser);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                TextView gtid = findViewById(R.id.gtid_actual);
                gtid.setText(currentUser.getGtid());

                TextView email = findViewById(R.id.email_actual);
                email.setText(currentUser.getEmail());

                TextView name = findViewById(R.id.name_actual);
                name.setText(currentUser.getFirst() + " " + currentUser.getLast());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
