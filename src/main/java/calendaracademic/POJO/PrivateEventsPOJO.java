package calendaracademic.POJO;

import java.util.Date;

public class PrivateEventsPOJO {

    private String name;
    private String event_description;
    private String location;
    private Date start_date;
    private Date end_date;
    private String start_hour;
    private String end_hour;
    private String owner;

    public PrivateEventsPOJO(String name, String event_description, String location, Date start_date, Date end_date,
                             String start_hour, String end_hour, String owner) {
        this.name = name;
        this.event_description = event_description;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.owner = owner;
    }

    public PrivateEventsPOJO(){}

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
