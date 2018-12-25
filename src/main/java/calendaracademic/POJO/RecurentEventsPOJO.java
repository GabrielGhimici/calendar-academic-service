package calendaracademic.POJO;

import java.util.Date;

public class RecurentEventsPOJO {

    private String name;
    private String event_description;
    private String location;
    private Date start_date;
    private Date end_date;
    private String start_hour;
    private String end_hour;
    private int frequency;
    private String recurring_days;

    public RecurentEventsPOJO(String name, String event_description, String location, Date start_date, Date end_date,
                              String start_hour, String end_hour, int frequency, String recurring_days) {
        this.name = name;
        this.event_description = event_description;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.frequency = frequency;
        this.recurring_days = recurring_days;
    }

    public RecurentEventsPOJO(){}

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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getRecurring_days() {
        return recurring_days;
    }

    public void setRecurring_days(String recurring_days) {
        this.recurring_days = recurring_days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
