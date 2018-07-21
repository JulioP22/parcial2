package internalLogic;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;
    @ManyToOne(fetch = FetchType.EAGER)
    private User receiver;
    private String reason;
    private Date date;
    private int state = 0;

    public Request(User sender, User receiver, String reason, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.reason = reason;
        this.date = date;
    }

    public Request() {
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "Request{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", reason='" + reason + '\'' +
                ", date=" + date +
                ", state=" + state +
                '}';
    }
}
