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
                        //TODO: Add to database
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
                        DatabaseReference ref = Database.getReference("events");
                        Event e = new Event(eventName, eventStartDay, eventStartTime, eventEndDay, eventEndTime);
                        // TODO: create hash for unique events so that reference is ref.child(user.getUid() / unique event hash id )
                        ref.child(user.getUid()).setValue(e.toMap());
                        // supposed to check if the write completes successfully or not, currently crashes the application
//                        ref.child(user.getUid()).setValue(e.toMap(), new DatabaseReference.CompletionListener() {
//                            public void onComplete(DatabaseError error, DatabaseReference ref) {
//                                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        Toast.makeText(getActivity(), "successfully added event to database!", Toast.LENGTH_SHORT).show();
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
