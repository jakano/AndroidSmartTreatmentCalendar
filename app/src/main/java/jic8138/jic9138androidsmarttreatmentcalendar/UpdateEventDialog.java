package jic8138.jic9138androidsmarttreatmentcalendar;

import android.app.Activity;
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
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class UpdateEventDialog extends DialogFragment {

    private static final int APPOINTMENT = 0;
    private static final int SPORT = 1;


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

    private Event mEvent;

    private DialogInterface.OnDismissListener onDismissListener;

    public UpdateEventDialog() {
    }

    public static UpdateEventDialog newInstance(Event event) {
        UpdateEventDialog updateEventDialog = new UpdateEventDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("event", event);
        updateEventDialog.setArguments(bundle);
        return updateEventDialog;
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

        //Set up EventType Spinner
        setUpSpinner();

        if(getArguments() != null) {
            mEvent = getArguments().getParcelable("event");
            preFill(mEvent);
        }

        //Set inputs of the Start/End Day/Time textFields to be their respective pickers.
        setUpTimePickers();
        setUpDatePickers();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle(R.string.calendar_update_event)
                // Add action buttons
                .setPositiveButton(R.string.calendar_update_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(isUpdated(mEvent)) {
                            updateEventOnDatabase(dialog);
                        }
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(UpdateEventDialog.this.getContext(),
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
                            UpdateEventDialog.this.getContext(),
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
                            UpdateEventDialog.this.getContext(),
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
                            UpdateEventDialog.this.getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(
                                        TimePicker timePicker,
                                        int selectedHour,
                                        int selectedMinute) {

                                    String auxillary = "AM";

                                    if (selectedHour > 12) {
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
                            UpdateEventDialog.this.getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(
                                        TimePicker timePicker,
                                        int selectedHour,
                                        int selectedMinute) {

                                    String auxillary = "AM";

                                    if (selectedHour > 12) {
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
     * Pre-fills the Dialog with the current events info
     * @param event, the current event
     */
    private void preFill(Event event) {
        mEventNameTextField.setText(event.getEventName());
        mEventStartDayTextField.setText(event.getEventStartDay());
        mEventEndDayTextField.setText(event.getEventEndDay());
        mEventStartTimeTextField.setText(event.getEventStartTime());
        mEventEndTimeTextField.setText(event.getEventEndTime());
        if(mEvent.getEventType().equals("Appointment")) {
            mEventTypeSpinner.setSelection(APPOINTMENT);
        } else {
            mEventTypeSpinner.setSelection(SPORT);
        }
    }

    /**
     * Checks if any of the Event Information has been altered from its pre-fill state.
     * @param event , Event used to compare the original state of the input fields.
     * @return true of any of the fields have changed, false otherwise.
     */
    private boolean isUpdated(Event event) {
        return !mEventNameTextField.getText().toString().trim().equals(event.getEventName())
                || !mEventStartDayTextField.getText().toString().trim().equals(event.getEventStartDay())
                || !mEventEndDayTextField.getText().toString().trim().equals(event.getEventEndDay())
                || !mEventStartTimeTextField.getText().toString().trim().equals(event.getEventStartTime())
                || !mEventEndTimeTextField.getText().toString().trim().equals(event.getEventEndTime())
                || !mEventTypeSpinner.getSelectedItem().toString().trim().equals(event.getEventType());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    /**
     * Update Events to the the Firebase Database.
     * @param dialog interface for dismissing dialogs used to add events to the DB
     */
    private void updateEventOnDatabase(DialogInterface dialog) {
        String eventName = mEventNameTextField.getText().toString().trim();
        String eventStartDay = mEventStartDayTextField.getText().toString().trim();
        String eventStartTime = mEventStartTimeTextField.getText().toString().trim();
        String eventEndDay = mEventEndDayTextField.getText().toString().trim();
        String eventEndTime = mEventEndTimeTextField.getText().toString().trim();
        String eventType = mEventTypeSpinner.getSelectedItem().toString().trim();
        String eventID = mEvent.getEventID();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String eventUser = user.getUid();

        DatabaseReference ref = Database.getReference("events").child(eventID);
        Event e = new Event(eventID, eventName, eventStartDay, eventStartTime, eventEndDay, eventEndTime, eventType, eventUser);
        ref.setValue(e.toMap());

        Toast.makeText(getActivity(), "Event updated!", Toast.LENGTH_SHORT).show();

    }
}
