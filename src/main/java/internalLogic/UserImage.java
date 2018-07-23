package internalLogic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;

@Entity
public class UserImage extends Publication{

    @Column( name = "IMAGE" )
    @Lob
    private byte[] image = null;
    @Transient
    private String strImage;

    public UserImage(byte[] image, Set<Comment> commentSet, Set<MLike> likeSet, Date date, User creator, String description, Set<User> taggedUsers, User receiverUser) {
        super(commentSet, likeSet, date, creator, description, taggedUsers, receiverUser);
        this.image = image;
    }

    public boolean checkVariable(){
        return this.image == null;
    }

    public String getBase64Image(){
        String image = null;
        try {
            if (this.image != null) image = new String(this.image,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return image;
    }

    public UserImage() {
        super();
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getStrImage() {
        return strImage;
    }

    public void setStrImage(String strImage) {
        this.strImage = strImage;
    }

    @Override
    public String toString() {
        return "UserImage{" +
                "image=" + Arrays.toString(image) +
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
