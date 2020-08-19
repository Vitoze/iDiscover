package ua.cm.idiscover.BeginMenu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Created by Ana on 03/04/2018.
 */

public class Event {

    private String type;
    private String id;
    private String name;
    private String person;
    private Date time;
    private String place;
    private String presentation;

    public String getName() {
        return name;
    }

    public String getPerson() {
        return person;
    }

    public Date getTime() { return time; }

    public String getPlace() {
        return place;
    }

    public String getPresentation() {
        return presentation;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Event(String id, String type, String name, String person, Date time, String place, String presentation) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.person = person;
        this.time = time;
        this.place = place;
        this.presentation = presentation;
    }
}
