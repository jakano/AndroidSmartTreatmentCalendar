package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.DialogInterface;
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

import java.util.ArrayList;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class DetailedEventActivity extends AppCompatActivity {

    private final static String UPDATE_EVENT = "update_events";

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

        displayEventInfo(mEvent);

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateTap();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override

















































            public void onClick(View v) {
                onDeleteTap();
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
        ((UpdateEventDialog) updateEventDialog).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ArrayList<Event> events = Database.getEvents();
                for (Event event : events) {
                    if (event.getEventID().equals(mEvent.getEventID())) {
                        mEvent = event;
                        displayEventInfo(event);
                        sendBroadcast(new Intent(UPDATE_EVENT));
                        break;
                    }
                }
            }
        });
        updateEventDialog.show(getSupportFragmentManager(), "Update Event");

    }

    private void onDeleteTap() {
        DatabaseReference ref = Database.getReference("events");
        ref.child(mEvent.getEventID()).removeValue();
        Toast.makeText(this, "Event deleted!", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(UPDATE_EVENT));
        finish();
    }

    private void onDismissTap() {
        finish();
    }

    public void displayEventInfo(Event event) {
        mTitleTextView.setText(event.getEventName());

        String startDate = String.format("%s, at %s", event.getEventStartDay(), event.getEventStartTime());
        mStartDateTextView.setText(startDate);

        String endDate = String.format("%s, at %s", event.getEventEndDay(), event.getEventEndTime());
        mEndDateTextView.setText(endDate);


        DatabaseReference ref = Database.getReference("users");
        ref.child(event.getEventUser()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                String article = "Sport".equals(event.getEventType()) ? "A" : "An";
                String eventTypeDetails = String.format("%s %s event by %s", article, event.getEventType(), fullName);
                mEventTypeTextView.setText(eventTypeDetails);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }
}
