package jic8138.jic9138androidsmarttreatmentcalendar;


import android.os.Parcel;
import android.os.Parcelable;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Event implements Parcelable{
    private String eventID;
    private String eventName;
    private String eventStartDay;
    private String eventStartTime;
    private String eventEndDay;
    private String eventEndTime;
    private String eventType;
    private String eventUser;

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Event(String eventID, String eventName, String eventStartDay, String eventStartTime, String eventEndDay, String eventEndTime, String eventType, String eventUser) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStartDay = eventStartDay;
        this.eventStartTime = eventStartTime;
        this.eventEndDay = eventEndDay;
        this.eventEndTime = eventEndTime;
        this.eventType = eventType;
        this.eventUser = eventUser;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventID", eventID);
        result.put("eventName", eventName);
        result.put("eventStartDay", eventStartDay);
        result.put("eventStartTime", eventStartTime);
        result.put("eventEndDay", eventEndDay);
        result.put("eventEndTime", eventEndTime);
        result.put("eventType", eventType);
        result.put("eventUser", eventUser);
        return result;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventStartDay() {
        return eventStartDay;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }
    public String getEventEndDay() {
        return eventEndDay;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventUser() {
        return eventUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Event(Parcel in) {
        eventID = in.readString();
        eventName = in.readString();
        eventStartDay = in.readString();
        eventStartTime = in.readString();
        eventEndDay = in.readString();
        eventEndTime = in.readString();
        eventType = in.readString();
        eventUser = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventID);
        dest.writeString(eventName);
        dest.writeString(eventStartDay);
        dest.writeString(eventStartTime);
        dest.writeString(eventEndDay);
        dest.writeString(eventEndTime);
        dest.writeString(eventType);
        dest.writeString(eventUser);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int[] retrieveDateInfo(String date) {
        String monthDayYear[] = date.split("/");
        //The WeekViewLibrary month recognition is off by 1
        int month = Integer.parseInt(monthDayYear[0]) - 1;
        int day = Integer.parseInt(monthDayYear[1]);
        int year = Integer.parseInt(monthDayYear[2]);
        int splitDateInfo[] = {month, day, year};

        return splitDateInfo;
    }

    public int[] retrieveTimeInfo(String time) {
        String hourAndMinute[] = time.split(":");
        int hour = Integer.parseInt(hourAndMinute[0]);
        String pureMinute = hourAndMinute[1].substring(0,2);
        int minute = Integer.parseInt(pureMinute);
        int splitTimeInfo[] = {hour, minute};

        return splitTimeInfo;
    }

    public WeekViewEvent getWeekViewEvent() {
        int startDateInfo[] = retrieveDateInfo(eventStartDay);
        int startTimeInfo[] = retrieveTimeInfo(eventStartTime);
        int endDateInfo[] = retrieveDateInfo(eventEndDay);
        int endTimeInfo[] = retrieveTimeInfo(eventEndTime);

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, startTimeInfo[0]);
        startTime.set(Calendar.MINUTE, startTimeInfo[1]);
        startTime.set(Calendar.DAY_OF_MONTH, startDateInfo[1]);
        startTime.set(Calendar.MONTH, startDateInfo[0]);
        startTime.set(Calendar.YEAR, startDateInfo[2]);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, endTimeInfo[0]);
        endTime.set(Calendar.MINUTE, endTimeInfo[1]);
        endTime.set(Calendar.MONTH, endDateInfo[0]);
        endTime.set(Calendar.DAY_OF_MONTH, endDateInfo[1]);
        endTime.set(Calendar.YEAR, endDateInfo[2]);

        return new WeekViewEvent(6, eventName, startTime, endTime);
    }
}