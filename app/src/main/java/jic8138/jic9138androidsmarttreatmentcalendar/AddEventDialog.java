package jic8138.jic9138androidsmarttreatmentcalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class AddEventDialog extends DialogFragment {
    private EditText mEventNameTextField;
    private EditText mEventStartDayTextField;
    private EditText mEventStartTimeTextField;
    private EditText mEventEndDayTextField;
    private EditText mEventEndTimeTextField;
    private Spinner mEventTypeSpinner;

    private boolean mIsStartTimePickerShowing;
    private boolean mIsEndTimePickerShowing;
    private boolean mIsStartDayPickerShowing;
    private boolean mIsEndDayPickerShowing;




    public AddEventDialog() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Database.initialize();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_event_dialog, null);
        mEventNameTextField = view.findViewById(R.id.add_event_name_input);
        mEventStartDayTextField = view.findViewById(R.id.add_event_start_day_input);
        mEventStartTimeTextField = view.findViewById(R.id.add_event_start_time_input);
        mEventEndDayTextField = view.findViewById(R.id.add_event_end_day_input);
        mEventEndTimeTextField = view.findViewById(R.id.add_event_end_time_input);
        mEventTypeSpinner = view.findViewById(R.id.add_event_type_dropdown);

        //Set inputs of the Start/End Day/Time textFields to be their respective pickers.
        setUpTimePickers();
        setUpDatePickers();
        //Set up EventType Spinner
        setUpSpinner();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle(R.string.calendar_add_event)
                // Add action buttons
                .setPositiveButton(R.string.calendar_add_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addEventToDatabase(dialog);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AddEventDialog.this.getContext(),
                R.array.event_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEventTypeSpinner.setAdapter(adapter);
    }
    /**
     * Replaces the Start and End DayTextField's input with a DatePicker.
     * Checks if the DatePicker is already showing to account for it showing twice
     * on devices running Android 4.0
     */
    private void setUpDatePickers() {
        mEventStartDayTextField.setInputType(InputType.TYPE_NULL);
        mEventEndDayTextField.setInputType(InputType.TYPE_NULL);

        mEventStartDayTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!mIsStartDayPickerShowing) {
                    mIsStartDayPickerShowing = true;
                    Calendar currentDay = Calendar.getInstance();
                    int year = currentDay.get(Calendar.YEAR);
                    int month = currentDay.get(Calendar.MONTH);
                    int day = currentDay.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog;
                    datePickerDialog = new DatePickerDialog(
                            AddEventDialog.this.getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int dayOfMonth) {
                            //Added 1 to month as it s always 1 off.
                            selectedMonth += 1;
                            String selectedDate = String.format("%02d/%02d/%02d", selectedMonth, dayOfMonth, selectedYear);
                            mEventStartDayTextField.setText(selectedDate);
                        }
                    }, year, month, day);
                    datePickerDialog.setTitle("Select Start Time");

                    datePickerDialog.show();
                    mEventStartDayTextField.clearFocus();
                } else {
                    mIsStartDayPickerShowing = false;
                }
            }
        });

        mEventEndDayTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!mIsEndDayPickerShowing) {
                    mIsEndDayPickerShowing = true;
                    Calendar currentDay = Calendar.getInstance();
                    int year = currentDay.get(Calendar.YEAR);
                    int month = currentDay.get(Calendar.MONTH);
                    int day = currentDay.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog;
                    datePickerDialog = new DatePickerDialog(
                            AddEventDialog.this.getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int dayOfMonth) {
                                    //Added 1 to month as it s always 1 off.
                                    selectedMonth += 1;
                                    String selectedDate = String.format("%02d/%02d/%02d", selectedMonth, dayOfMonth, selectedYear);
                                    mEventEndDayTextField.setText(selectedDate);
                                }
                            }, year, month, day);
                    datePickerDialog.setTitle("Select Start Time");

                    datePickerDialog.show();
                    mEventEndDayTextField.clearFocus();
                } else {
                    mIsEndDayPickerShowing = false;
                }
            }
        });
    }

    /**
     * Replaces the Start and End TimeTextField's input with a TimePicker.
     * Checks if the TimePicker is already showing to account for it showing twice
     * on devices running Android 4.0
     */
    private void setUpTimePickers() {
        mEventStartTimeTextField.setInputType(InputType.TYPE_NULL);
        mEventEndTimeTextField.setInputType(InputType.TYPE_NULL);
        mEventStartTimeTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!mIsStartTimePickerShowing) {
                    mIsStartTimePickerShowing = true;
                    Calendar currentTime = Calendar.getInstance();
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = currentTime.get(Calendar.MINUTE);
                    TimePickerDialog timePicker;
                    timePicker = new TimePickerDialog(
                            AddEventDialog.this.getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(
                                TimePicker timePicker,
                                int selectedHour,
                                int selectedMinute) {

                            String auxillary = "AM";

                            if (selectedHour > 12) {
                                selectedHour = selectedHour - 12;
                                auxillary = "PM";
                            }

                            String selectedStartTime = String.format(
                                    "%02d:%02d %s",
                                    selectedHour,
                                    selectedMinute,
                                    auxillary);
                            mEventStartTimeTextField.setText(selectedStartTime);
                        }
                    }, hour, minute, false);//No 24 hour time
                    timePicker.setTitle("Select Start Time");
                    timePicker.show();
                    mEventStartTimeTextField.clearFocus();
                } else {
                    mIsStartTimePickerShowing = false;
                }
            }
        });

        mEventEndTimeTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!mIsEndTimePickerShowing) {
                    mIsEndTimePickerShowing = true;
                    Calendar currentTime = Calendar.getInstance();
                    int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = currentTime.get(Calendar.MINUTE);
                    TimePickerDialog timePicker;
                    timePicker = new TimePickerDialog(
                            AddEventDialog.this.getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(
                                TimePicker timePicker,
                                int selectedHour,
                                int selectedMinute) {

                            String auxillary = "AM";

                            if (selectedHour > 12) {
                                selectedHour = selectedHour - 12;
                                auxillary = "PM";
                            }

                            String selectedEndTime = String.format(
                                    "%02d:%02d %s",
                                    selectedHour,
                                    selectedMinute,
                                    auxillary);
                            mEventEndTimeTextField.setText(selectedEndTime);
                        }
                    }, hour, minute, false);//No 24 hour time
                    timePicker.setTitle("Select Start Time");

                    timePicker.show();
                    mEventEndTimeTextField.clearFocus();
                } else {
                    mIsEndTimePickerShowing = false;
                }
            }
        });
    }

    /**
     * Adds Events to the the Firebase Database.
     * @param dialog interface for dismissing dialogs used to add events to the DB
     */
    private void addEventToDatabase(DialogInterface dialog) {
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
}
