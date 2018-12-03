package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventsListAdapter extends ArrayAdapter<Event> {

    private static int BUZZ_GOLD = R.color.buzz_gold;
    private static int LIGHTER_BUZZ_BLUE = R.color.lighter_buzz_blue;

    private final Context mContext;

    private  ArrayList<Event> mEvents;


    public EventsListAdapter(Context context, ArrayList<Event> events) {
        super(context, R.layout.events_list_row, events);

        mEvents = events;
        mContext = context;

    }

    public void setEvents(ArrayList<Event> newEvents) {
        mEvents = newEvents;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView title;
        TextView times;

    }

    @Override
    public int getCount() {
        return mEvents.size();
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
            String title = String.format(
                    "%s (%s)",
                    currentEvent.getEventName(),
                    currentEvent.getEventType());
            viewHolder.title.setText(title);
            int color = "Sport".equals(currentEvent.getEventType()) ? LIGHTER_BUZZ_BLUE : BUZZ_GOLD;
            viewHolder.title.setTextColor(convertView.getResources().getColor(color));

            String time_range = String.format(
                    "From %s to %s",
                    currentEvent.getEventStartTime(),
                    currentEvent.getEventEndTime());
            viewHolder.times.setText(time_range);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToDetailedEventActivity(currentEvent);
                }
            });
        }
        return convertView;

    }

    private void goToDetailedEventActivity(Event event) {
        Intent intent = new Intent(getContext(), DetailedEventActivity.class);
        intent.putExtra("event", event);
        getContext().startActivity(intent);
    }
}
