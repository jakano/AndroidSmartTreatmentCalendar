package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthViewFragment extends Fragment {

    private final static String UPDATE_EVENT = "update_events";

    private ArrayList<Event> mEvents;
    private int mSelectedDay;

    private CalendarView mCalendar;
    private ListView mEventsListView;
    private EventsListAdapter mEventsListAdapter;
    private TextView mEmptyListTextView;

    private OnFragmentInteractionListener mListener;
    private BroadcastReceiver mReceiver;

    public MonthViewFragment() {
        // Required empty public constructor
    }


    public static MonthViewFragment newInstance() {
        MonthViewFragment fragment = new MonthViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvents = Database.getEvents();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month_view, container, false);
        mCalendar = view.findViewById(R.id.calendarView);
        mEventsListView = view.findViewById(R.id.calendar_event_list);
        mEventsListView.setVisibility(View.VISIBLE);
        mEmptyListTextView = view.findViewById(R.id.calendar_no_events);
        updateCalendar();

        //If the current day of the calendar is already set to a day with events, show list.
        int firstDay = mCalendar.getFirstSelectedDate().get(Calendar.DAY_OF_MONTH);;
        updateEventList(firstDay);

        mCalendar.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                mSelectedDay = eventDay.getCalendar().get(Calendar.DAY_OF_MONTH);

                updateEventList(mSelectedDay);
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (UPDATE_EVENT.equals(intent.getAction())) {
                    updateCalendar();
                }
            }
        };
        IntentFilter filter = new IntentFilter(UPDATE_EVENT);
        getContext().registerReceiver(mReceiver, filter);
        return view;
    }

    /**
     * Updates the List of Events with Events on a current day
     * @param day of which the Events are searched for.
     */
    private void updateEventList(int day) {
        ArrayList<Event> eventsOnDay = getEventsOnDay(day);

        //Display Events in a  list associated with the selected day
        if(mEventsListAdapter == null) {
            mEventsListAdapter = new EventsListAdapter(getContext(), eventsOnDay);
            mEventsListView.setAdapter(mEventsListAdapter);

        } else {
            mEventsListAdapter.setEvents(eventsOnDay);
        }
        shouldShowEmptyListTextViey(eventsOnDay.size());
    }

    /**
     * Search through all Events to find the Events of a specific day
     * @param day , the day of the events being searched for.
     * @return An ArrayList containing the events on a specific day
     */
    private ArrayList<Event> getEventsOnDay(int day) {
        ArrayList<Event> eventsOnDay = new ArrayList<>();
        for (Event event : mEvents) {
            int eventDay = event.retrieveDateInfo(event.getEventStartDay())[1];
            if(eventDay == day) {
                eventsOnDay.add(event);
            }
        }
        return eventsOnDay;
    }

    /**
     * Hide the EventListView if there are no events. Show a message stating there are no evets
     * @param size, the size of the list of available events.
     */
    private void shouldShowEmptyListTextViey(int size) {
        mEmptyListTextView.setVisibility(size == 0? View.VISIBLE : View.GONE);
        mEventsListView.setVisibility( size != 0? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(UPDATE_EVENT);
        getContext().registerReceiver(mReceiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mReceiver);
    }

    public void updateCalendar() {
        List<EventDay> events = new ArrayList<>();
        mEvents = Database.getEvents();

        for (Event event : mEvents) {
            events.add(new EventDay(event.getCalendarEvent(), R.drawable.calendar_event_icons));
        }

        //Set Events of Calendar
        mCalendar.setEvents(events);
        updateEventList(mSelectedDay);
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
