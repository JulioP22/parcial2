package internalLogic;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Notification {

    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Publication publication;
    private Date date;
    private int state = 0;

    public Notification(String description, User user, Publication publication, Date date) {
        this.description = description;
        this.user = user;
        this.publication = publication;
        this.date = date;
    }

    public Notification() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "description='" + description + '\'' +
                ", user=" + user +
                ", id=" + id +
                ", publication=" + publication +
                ", date=" + date +
                ", state=" + state +
                '}';
    }
}
