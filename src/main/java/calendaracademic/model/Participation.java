package calendaracademic.model;

import javax.persistence.*;

@Entity
@Table(name = "\"participation\"")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participation_id",nullable = false )
    private long id;

    @Column(name = "recurent_event")
    private long recurent_event;

    @Column(name = "private_event")
    private long private_event;

    @Column(name = "normal_event")
    private long normal_event;

    @Column(name = "private_recurent_event")
    private long private_recurent_event;

    @Column(name = "preffered")
    private boolean preffered;

    @Column(name = "own_start_time")
    private String own_start_time;

    @Column(name = "own_end_time")
    private String own_end_time;

    @Column(name = "user")
    private long user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecurent_event() {
        return recurent_event;
    }

    public void setRecurent_event(long recurent_event) {
        this.recurent_event = recurent_event;
    }

    public long getPrivate_event() {
        return private_event;
    }

    public void setPrivate_event(long private_event) {
        this.private_event = private_event;
    }

    public long getNormal_event() {
        return normal_event;
    }

    public void setNormal_event(long normal_event) {
        this.normal_event = normal_event;
    }

    public long getPrivate_recurent_event() {
        return private_recurent_event;
    }

    public void setPrivate_recurent_event(long private_recurent_event) {
        this.private_recurent_event = private_recurent_event;
    }

    public boolean isPreffered() {
        return preffered;
    }

    public void setPreffered(boolean preffered) {
        this.preffered = preffered;
    }

    public String getOwn_start_time() {
        return own_start_time;
    }

    public void setOwn_start_time(String own_start_time) {
        this.own_start_time = own_start_time;
    }

    public String getOwn_end_time() {
        return own_end_time;
    }

    public void setOwn_end_time(String own_end_time) {
        this.own_end_time = own_end_time;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }
}
