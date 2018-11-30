package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeekViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeekViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Event> mEvents;
    private String mParam2;

    private WeekView mSevenDayWeekView;

    private OnFragmentInteractionListener mListener;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    public static WeekViewFragment newInstance() {
        WeekViewFragment fragment = new WeekViewFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEvents = Database.getEvents();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MonthLoader.MonthChangeListener monthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.

                ArrayList<WeekViewEvent> weekViewEvents =  new ArrayList<>();
                for (int i = 0; i < mEvents.size(); i++) {
                    //This method is run for the previous, current, and next month.
                    // We only want to create WeekViewEvent objects on the current month
                    Event currentEvent = mEvents.get(i);
                    long weekDayEventID = (long)i;
                    int eventStartDateMonth = currentEvent.retrieveDateInfo(currentEvent.getEventStartDay())[0];
                    if(eventStartDateMonth == newMonth - 1) {
                        WeekViewEvent weekViewEvent = currentEvent.getWeekViewEvent();
                        weekViewEvent.setColor(getResources().getColor(R.color.buzz_gold));
                        weekViewEvent.setId(weekDayEventID);
                        weekViewEvents.add(weekViewEvent);
                    }
                }
                return weekViewEvents;
            }
        };


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_week_view, container, false);
        mSevenDayWeekView = view.findViewById(R.id.calendar_week_view);
        mSevenDayWeekView.setMonthChangeListener(monthChangeListener);
        mSevenDayWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mSevenDayWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mSevenDayWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mSevenDayWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                int eventPos = (int)event.getId();
                Event tappedEvent = mEvents.get(eventPos);
                goToDetailedEventActivity(tappedEvent);
            }
        });
        setupDateTimeInterpreter();
        return view;
    }

    /**
     * Set up a date time interpreter which will show short date values
     */
    private void setupDateTimeInterpreter() {
        mSevenDayWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    private void goToDetailedEventActivity(Event event) {
        Intent intent = new Intent(getActivity(), DetailedEventActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
