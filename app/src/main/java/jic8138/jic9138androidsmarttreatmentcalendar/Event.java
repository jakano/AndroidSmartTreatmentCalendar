package jic8138.jic9138androidsmarttreatmentcalendar;


import java.util.HashMap;
import java.util.Map;

public class Event {
    private String eventID;
    private String eventName;
    private String eventStartDay;
    private String eventStartTime;
    private String eventEndDay;
    private String eventEndTime;

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Event(String eventID, String eventName, String eventStartDay, String eventStartTime, String eventEndDay, String eventEndTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStartDay = eventStartDay;
        this.eventStartTime = eventStartTime;
        this.eventEndDay = eventEndDay;
        this.eventEndTime = eventEndTime;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventID", eventID);
        result.put("eventName", eventName);
        result.put("eventStartDay", eventStartDay);
        result.put("eventStartTime", eventStartTime);
        result.put("eventEndDay", eventEndDay);
        result.put("eventEndTime", eventEndTime);
        return result;
    }


}