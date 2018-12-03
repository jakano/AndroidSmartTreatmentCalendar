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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class UpdateEventDialog extends DialogFragment {

    private static final int APPOINTMENT = 0;
    private static final int SPORT = 1;


    private EditText mEventNameTextField;
    private EditText mEventDayTextField;
    private EditText mEventStartTimeTextField;
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
        mEventDayTextField = view.findViewById(R.id.add_event_start_day_input);
        mEventStartTimeTextField = view.findViewById(R.id.add_event_start_time_input);
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
                        // replaced by onResume()
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        // replaces the original POSITIVE button for add event
        // thus trying to add an event with wrong credentials doesn't exit the dialog on default
        super.onResume();
        AlertDialog dialog = (AlertDialog) getDialog();
        Button addEventButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdated(mEvent)) {
                    updateEventOnDatabase(dialog);
                } else {
                    Toast.makeText(getActivity(), "No Event details were changed", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
        mEventDayTextField.setInputType(InputType.TYPE_NULL);

        mEventDayTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                                    mEventDayTextField.setText(selectedDate);
                                }
                            }, year, month, day);
                    datePickerDialog.setTitle("Select Start Time");

                    datePickerDialog.show();
                    mEventDayTextField.clearFocus();
                } else {
                    mIsStartDayPickerShowing = false;
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
        mEventDayTextField.setText(event.getEventStartDay());
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
                || !mEventDayTextField.getText().toString().trim().equals(event.getEventStartDay())
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
        String eventStartDay = mEventDayTextField.getText().toString().trim();
        String eventStartTime = mEventStartTimeTextField.getText().toString().trim();
        String eventEndDay = mEventDayTextField.getText().toString().trim();
        String eventEndTime = mEventEndTimeTextField.getText().toString().trim();
        String eventType = mEventTypeSpinner.getSelectedItem().toString().trim();
        String eventID = mEvent.getEventID();

        Boolean isValidEvent = true;

        if (eventName == null || eventName.isEmpty()) {
            isValidEvent = false;
            Toast.makeText(getActivity(), "Event Name is empty", Toast.LENGTH_SHORT).show();
        } else if (eventStartDay == null || eventStartDay.isEmpty()) {
            isValidEvent = false;
            Toast.makeText(getActivity(), "Event Day is empty", Toast.LENGTH_SHORT).show();
        } else if (eventStartTime == null || eventStartTime.isEmpty()) {
            isValidEvent = false;
            Toast.makeText(getActivity(), "Event Start Time is empty", Toast.LENGTH_SHORT).show();
        } else if (eventEndTime == null || eventEndTime.isEmpty()) {
            isValidEvent = false;
            Toast.makeText(getActivity(), "Event End Time is empty", Toast.LENGTH_SHORT).show();
        } else {
            // parse start time
            String startHourAndMinute[] = eventStartTime.split(":");
            int startHour = Integer.parseInt(startHourAndMinute[0]);
            String pureStartMinute = startHourAndMinute[1].substring(0,2);
            int startMinute = Integer.parseInt(pureStartMinute);
            String startAMPM = eventStartTime.substring(6,8);
            // parse end time
            String endHourAndMinute[] = eventEndTime.split(":");
            int endHour = Integer.parseInt(endHourAndMinute[0]);
            String pureEndMinute = endHourAndMinute[1].substring(0,2);
            int endMinute = Integer.parseInt(pureEndMinute);
            String endAMPM = eventEndTime.substring(6,8);

            Log.d("TIMES", startHour + "+" + startMinute + "+" + startAMPM);
            Log.d("TIMES", endHour + "+" + endMinute + "+" + endAMPM);
            // check if start time > end time
            if (startAMPM.equals("PM") && endAMPM.equals("AM")) {
                isValidEvent = false;
            } else if (startAMPM.equals("PM") && endAMPM.equals("PM")) {
                if (startHour > endHour) {
                    isValidEvent = false;
                } else if (startHour == endHour) {
                    if (startMinute > endMinute) {
                        isValidEvent = false;
                    }
                }
            } else if (startAMPM.equals("AM") && endAMPM.equals("AM")) {
                if (startHour > endHour) {
                    isValidEvent = false;
                } else if (startHour == endHour) {
                    if (startMinute > endMinute) {
                        isValidEvent = false;
                    }
                }
            }
            if (!isValidEvent) {
                Toast.makeText(getActivity(), "Invalid Start and End Times", Toast.LENGTH_SHORT).show();
            }


        }

        if (isValidEvent) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String eventUser = user.getUid();

            DatabaseReference ref = Database.getReference("events").child(eventID);
            Event e = new Event(eventID, eventName, eventStartDay, eventStartTime, eventEndDay, eventEndTime, eventType, eventUser);
            ref.setValue(e.toMap());
            dialog.dismiss();

            Toast.makeText(getActivity(), "Event updated!", Toast.LENGTH_SHORT).show();
        }

    }
}
