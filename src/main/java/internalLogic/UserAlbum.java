package internalLogic;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
public class UserAlbum extends Publication {
    @OneToMany
    private Set<UserImage> userImages;

    public UserAlbum(Set<UserImage> userImages, String description, User creator, Date date, Set<Comment> commentSet, Set<MLike> likeSet, long id, Set<User> taggedUsers, User receiverUser) {
        super(id, commentSet, likeSet,date, creator, description, taggedUsers, receiverUser);
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

}
