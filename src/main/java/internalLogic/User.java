package internalLogic;

import javax.persistence.*;
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
    @Column( name = "PORTRAITPHOTO" )
    @Lob
    private byte[] portraitPhoto;


    public User(String fullName, String email, String password, Date bornDate, String sex, String location, String jobs, Set<User> friends, String role, byte[] profilePhoto, byte[] portraitPhoto) {
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
        this.portraitPhoto = portraitPhoto;
    }

    public User() {
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

    public byte[] getPortraitPhoto() {
        return portraitPhoto;
    }

    public void setPortraitPhoto(byte[] portraitPhoto) {
        this.portraitPhoto = portraitPhoto;
    }
}
