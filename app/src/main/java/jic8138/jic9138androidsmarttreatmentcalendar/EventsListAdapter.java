package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventsListAdapter extends ArrayAdapter<Event> {

    private final Context mContext;

    private  ArrayList<Event> mEvents;


    public EventsListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.events_list_row, events);

        mEvents = events;
        mContext = context;

    }

    public void setEvents(ArrayList<Event> newEvents) {
        mEvents = newEvents;
    }
    //TODO: Use the Holder Pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.events_list_row, parent, false);
        }
        if(mEvents.size() != 0) {
            Event currentEvent = mEvents.get(position);

            TextView title = view.findViewById(R.id.events_list_title);
            TextView times = view.findViewById(R.id.events_list_time_range);

            title.setText(currentEvent.getEventName());
            String time_range = String.format("From %s to %s", currentEvent.getEventStartTime(), currentEvent.getEventEndTime());
            times.setText(time_range);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailedEventActivity(currentEvent);
                }
            });
        }


        return view;
    }

    private void goToDetailedEventActivity(Event event) {
        Intent intent = new Intent(getContext(), DetailedEventActivity.class);
        intent.putExtra("event", event);
        getContext().startActivity(intent);
    }
}
