package internalLogic;

import javax.persistence.*;

@Entity
public class Notification {

    private String description;
    @ManyToOne
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Publication publication;

    public Notification(String description, User user, Publication publication) {
        this.description = description;
        this.user = user;
        this.publication = publication;
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
}
