package calendaracademic.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "\"recurent_event\"")
public class Recurent_Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id",nullable = false )
    private long id;

    @Column(name = "name", columnDefinition = "text")
    private String name;

    @Column(name = "location", columnDefinition = "text")
    private String location;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "start_time")
    private String start_time;

    @Column(name = "end_time")
    private String end_time;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    //Recurring days are in format like Monday;Wednesday;Sunday
    @Column(name = "recurring_days")
    private String recurring_days;

    //Frequency describes how often the event will occur
    //Example: frequency is 2 => event will occur every two weeks
    @Column(name = "frequency")
    private int frequency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    public String getRecurring_days() {
        return recurring_days;
    }

    public void setRecurring_days(String recurring_days) {
        this.recurring_days = recurring_days;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
