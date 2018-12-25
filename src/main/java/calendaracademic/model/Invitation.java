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
    private Long recurent_event;

    @Column(name = "private_event")
    private Long private_event;

    @Column(name = "normal_event")
    private Long normal_event;

    @Column(name = "private_recurent_event")
    private Long private_recurent_event;

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
}
