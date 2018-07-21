package internalLogic;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
public class UserAlbum extends Publication {
    @OneToMany
    private Set<UserImage> userImages;

    public UserAlbum(Set<UserImage> userImages, String description, User creator, Date date, Set<Comment> commentSet, Set<MLike> likeSet, Set<User> taggedUsers, User receiverUser) {
        super(commentSet, likeSet,date, creator, description, taggedUsers, receiverUser);
        this.userImages = userImages;
    }

    public UserAlbum() {
        super();
    }

    public Set<UserImage> getUserImages() {
        return userImages;
    }

    public void setUserImages(Set<UserImage> userImages) {
        this.userImages = userImages;
    }

    @Override
    public String toString() {
        return "UserAlbum{" +
                "userImages=" + userImages +
                ", id=" + id +
                ", commentSet=" + commentSet +
                ", likeSet=" + likeSet +
                ", date=" + date +
                ", creator=" + creator +
                ", description='" + description + '\'' +
                ", taggedUsers=" + taggedUsers +
                ", receiverUser=" + receiverUser +
                ", liked=" + liked +
                '}';
    }
}
