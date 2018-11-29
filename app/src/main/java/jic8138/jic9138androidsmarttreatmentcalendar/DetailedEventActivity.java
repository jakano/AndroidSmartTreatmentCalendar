package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        //TODO: Add update button logic
    }

    private void onDeletTap() {
        //TODO: Add update button logic

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

        String eventTypeDetails = String.format("An %s Event by %s", mEvent.getEventType(), user);
        mEventTypeTextView.setText(eventTypeDetails);
    }
}
