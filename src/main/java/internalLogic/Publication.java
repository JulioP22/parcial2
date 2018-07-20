package internalLogic;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @OneToMany
    protected Set<Comment> commentSet;
    @OneToMany
    protected Set<MLike> likeSet;
    protected Date date;
    @ManyToOne
    protected User creator;
    protected String description;
    @ManyToMany
    protected Set<User> taggedUsers;
    @ManyToOne
    protected User receiverUser;

    public Publication(Set<Comment> commentSet, Set<MLike> likeSet, Date date, User creator, String description, Set<User> taggedUsers, User receiverUser) {
        this.commentSet = commentSet;
        this.likeSet = likeSet;
        this.date = date;
        this.creator = creator;
        this.description = description;
        this.taggedUsers = taggedUsers;
        this.receiverUser = receiverUser;
    }

    public Publication() {
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    public Set<MLike> getLikeSet() {
        return likeSet;
    }

    public void setLikeSet(Set<MLike> likeSet) {
        this.likeSet = likeSet;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(Set<User> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public User getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }
}
