package calendaracademic.model;

import javax.persistence.*;

@Entity
@Table(name = "\"invitation\"")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id",nullable = false )
    private long id;

    @Column(name = "user")
    private long user;

    @Column(name = "recurent_event")
    private long recurent_event;

    @Column(name = "private_event")
    private long private_event;

    @Column(name = "normal_event")
    private long normal_event;

    @Column(name = "private_recurent_event")
    private long private_recurent_event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
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
}
