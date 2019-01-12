package calendaracademic.model;

import javax.persistence.*;

@Entity
@Table(name = "\"participation\"")
public class Participation {

    @Id
    @Column(name = "participation_id",nullable = false )
    private Long participation_id;

    @Column(name = "recurent_event")
    private Long recurent_event;

    @Column(name = "private_event")
    private Long private_event;

    @Column(name = "normal_event")
    private Long normal_event;

    @Column(name = "private_recurent_event")
    private Long private_recurent_event;

    @Column(name = "preffered")
    private boolean preffered;

    @Column(name = "own_start_time")
    private String own_start_time;

    @Column(name = "own_end_time")
    private String own_end_time;

    @Column(name = "\"user\"")
    private Long user;

    public Long getParticipation_id() {
        return participation_id;
    }

    public void setParticipation_id(Long participation_id) {
        this.participation_id = participation_id;
    }

    public Long getRecurent_event() {
        return recurent_event;
    }

    public void setRecurent_event(Long recurent_event) {
        this.recurent_event = recurent_event;
    }

    public Long getPrivate_event() {
        return private_event;
    }

    public void setPrivate_event(Long private_event) {
        this.private_event = private_event;
    }

    public Long getNormal_event() {
        return normal_event;
    }

    public void setNormal_event(Long normal_event) {
        this.normal_event = normal_event;
    }

    public Long getPrivate_recurent_event() {
        return private_recurent_event;
    }

    public void setPrivate_recurent_event(Long private_recurent_event) {
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

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
