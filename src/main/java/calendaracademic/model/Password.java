package calendaracademic.model;

import javax.persistence.*;

@Entity
@Table(name = "Password")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "password_id",nullable = false )
    private long id;

    @Column(name = "pass_hashed", columnDefinition="text")
    private String pass;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
