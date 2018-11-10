package jic8138.jic9138androidsmarttreatmentcalendar;


import java.util.HashMap;
import java.util.Map;

public class User {

    private String gtid;
    private String email;
    private String first;
    private String last;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String gtid, String email, String first, String last) {
        this.gtid = gtid;
        this.email = email;
        this.first = first;
        this.last = last;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gtid", gtid);
        result.put("email", email);
        result.put("first", first);
        result.put("last", last);
        return result;
    }


}