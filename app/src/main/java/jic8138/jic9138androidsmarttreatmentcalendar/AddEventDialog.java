package jic8138.jic9138androidsmarttreatmentcalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class AddEventDialog extends DialogFragment {
    private EditText mEventNameTextField;
    private EditText mEventStartDayTextField;
    private EditText mEventStartTimeTextField;
    private EditText mEventEndDayTextField;
    private EditText mEventEndTimeTextField;

    public AddEventDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Database.initialize();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.add_event_dialog, null))
                .setTitle(R.string.calendar_add_event)
                // Add action buttons
                .setPositiveButton(R.string.calendar_add_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mEventNameTextField = getDialog().findViewById(R.id.add_event_name_input);
                        mEventStartDayTextField = getDialog().findViewById(R.id.add_event_start_day_input);
                        mEventStartTimeTextField = getDialog().findViewById(R.id.add_event_start_time_input);
                        mEventEndDayTextField = getDialog().findViewById(R.id.add_event_end_day_input);
                        mEventEndTimeTextField = getDialog().findViewById(R.id.add_event_end_time_input);

                        String eventName = mEventNameTextField.getText().toString().trim();
                        String eventStartDay = mEventStartDayTextField.getText().toString().trim();
                        String eventStartTime = mEventStartTimeTextField.getText().toString().trim();
                        String eventEndDay = mEventEndDayTextField.getText().toString().trim();
                        String eventEndTime = mEventEndTimeTextField.getText().toString().trim();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // Database ref to events/userID/eventID
                        DatabaseReference ref = Database.getReference("events").child(user.getUid()).push();
                        String eventID = ref.getKey();
                        Event e = new Event(eventID, eventName, eventStartDay, eventStartTime, eventEndDay, eventEndTime);
                        ref.setValue(e.toMap());

                        Toast.makeText(getActivity(), "Event created!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
