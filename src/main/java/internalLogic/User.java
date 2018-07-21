package internalLogic;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    @Column(unique = true)
    private String email;
    private String password;
    private Date bornDate;
    private String sex;
    private String location;
    private String jobs;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> friends;
    private String role;
    @Column( name = "PROFILEPHOTO" )
    @Lob
    private byte[] profilePhoto;


    public User(String fullName, String email, String password, Date bornDate, String sex, String location, String jobs, Set<User> friends, String role, byte[] profilePhoto) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.bornDate = bornDate;
        this.sex = sex;
        this.location = location;
        this.jobs = jobs;
        this.friends = friends;
        this.role = role;
        this.profilePhoto = profilePhoto;
    }

    public User() {
    }


    public String getBase64Image(){
        String image = "data:image/jpeg;base64,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";

        try {
            image = new String(this.profilePhoto,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
