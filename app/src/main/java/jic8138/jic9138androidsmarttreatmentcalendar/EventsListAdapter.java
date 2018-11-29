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

    private static class ViewHolder {
        TextView title;
        TextView times;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.events_list_row, parent, false);
            viewHolder.title = convertView.findViewById(R.id.events_list_title);
            viewHolder.times = convertView.findViewById(R.id.events_list_time_range);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mEvents.size() != 0 && position < mEvents.size()) {
            Event currentEvent = mEvents.get(position);
            viewHolder.title.setText(currentEvent.getEventName());

            String time_range = String.format(
                    "From %s to %s",
                    currentEvent.getEventStartTime(),
                    currentEvent.getEventEndTime());
            viewHolder.times.setText(time_range);
        }
        return convertView;

    }

    private void goToDetailedEventActivity(Event event) {
        Intent intent = new Intent(getContext(), DetailedEventActivity.class);
        intent.putExtra("event", event);
        getContext().startActivity(intent);
    }
}
