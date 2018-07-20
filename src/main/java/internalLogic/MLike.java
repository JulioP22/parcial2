package internalLogic;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MLike { // This name was given to avoid Sara's problem with the word Like in SQL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int action; // Could be 1 means a like and -1 means a dislike
    private Date date;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public MLike(int action, Date date, User user) {
        this.action = action;
        this.date = date;
        this.user = user;
    }

    public MLike() {
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
