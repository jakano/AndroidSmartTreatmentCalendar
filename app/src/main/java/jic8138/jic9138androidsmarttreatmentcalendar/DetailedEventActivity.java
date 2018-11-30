package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class DetailedEventActivity extends AppCompatActivity {

    TextView mTitleTextView;
    TextView mStartDateTextView;
    TextView mEndDateTextView;
    TextView mEventTypeTextView;

    Button mUpdateButton;
    Button mDeleteButton;
    Button mDismissButton;

    Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_event_screen);
        mTitleTextView = findViewById(R.id.detailed_event_title);
        mStartDateTextView = findViewById(R.id.detailed_event_start_date);
        mEndDateTextView = findViewById(R.id.detailed_event_end_date);
        mEventTypeTextView = findViewById(R.id.detailed_event_type);

        mUpdateButton = findViewById(R.id.detailed_event_update_button);
        mDeleteButton = findViewById(R.id.detailed_event_delete_button);
        mDismissButton = findViewById(R.id.detailed_event_dismiss_button);

        Intent intent = getIntent();
        mEvent = intent.getParcelableExtra("event");

        displayEventInfo();

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateTap();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeletTap();
            }
        });

        mDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDismissTap();
            }
        });
    }

    private void onUpdateTap() {
        DialogFragment updateEventDialog = UpdateEventDialog.newInstance(mEvent);
        updateEventDialog.show(getSupportFragmentManager(), "Update Event");
    }

    private void onDeletTap() {
        //TODO: Add Delete Event database logic
        DatabaseReference ref = Database.getReference("events");
        ref.child(mEvent.getEventID()).removeValue();
        Toast.makeText(this, "Event deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onDismissTap() {
        finish();
    }

    private void displayEventInfo() {
        mTitleTextView.setText(mEvent.getEventName());

        String startDate = String.format("%s, at %s", mEvent.getEventStartDay(), mEvent.getEventStartTime());
        mStartDateTextView.setText(startDate);

        String endDate = String.format("%s, at %s", mEvent.getEventEndDay(), mEvent.getEventEndTime());
        mEndDateTextView.setText(endDate);

        //TODO Fix User API call and replace default string with call.
        String user = "a User";
        DatabaseReference ref = Database.getReference("users");
        ref.child(mEvent.getEventUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = "";
                String lastName = "";
                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    String attributeKey = userData.getKey();
                    if ("first".equals(attributeKey)) {
                        firstName = userData.getValue(String.class);
                    } else if ("last".equals(attributeKey)) {
                        lastName = userData.getValue(String.class);
                    }
                }

                String fullName = firstName + " " + lastName;
                Log.d("FULLNAME", fullName);
                String eventTypeDetails = String.format("A %s Event by %s", mEvent.getEventType(), fullName);
                mEventTypeTextView.setText(eventTypeDetails);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }
}
