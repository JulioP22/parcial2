package internalLogic;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @OneToMany(fetch = FetchType.EAGER)
    protected Set<Comment> commentSet;
    @OneToMany(fetch = FetchType.EAGER)
    protected Set<MLike> likeSet;
    protected Date date;
    @ManyToOne(fetch = FetchType.EAGER)
    protected User creator;
    @Column(columnDefinition="text")
    protected String description;
    @ManyToMany(fetch = FetchType.EAGER)
    protected Set<User> taggedUsers;
    @ManyToOne(fetch = FetchType.EAGER)
    protected User receiverUser;
    @Transient
    protected boolean liked = false;

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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void verifyLike(User user){
        for(MLike i: likeSet){
            if (i.getUser().getId()==user.getId()){
                liked = true;
            }
        }
    }
}
