/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author alvintjw
 */
@Entity
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   private String profilePicture;
    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;
    private String password;
    private byte degree;
    private byte gender;
    private int academicYear;
    @Temporal(TemporalType.DATE)
    private Date dob;
    private String specialization;
    private boolean isUserAnonymous;
    private String anonymousName;
    @OneToMany
    private List<Course> pinnedCourses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes;
    
    @OneToMany
    private List<Chat> chats;

    public Student() {
    }

    public Student(String email, String contactnumber, String firstname, String lastname, String password) {
        this.email = email;
        this.contactnumber = contactnumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    /**
     * @return the degree
     */
    public byte getDegree() {
        return degree;
    }

    /**
     * @param degree the degree to set
     */
    public void setDegree(byte degree) {
        this.degree = degree;
    }

    /**
     * @return the gender
     */
    public byte getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(byte gender) {
        this.gender = gender;
    }

    /**
     * @return the year
     */
    public int getAcademicYear() {
        return academicYear;
    }

    /**
     * @param year the year to set
     */
    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public List<Course> getPinnedCourses() {
        return pinnedCourses;
    }

    public void setPinnedCourses(List<Course> pinnedCourses) {
        this.pinnedCourses = pinnedCourses;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return the specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * @param specialization the specialization to set
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * @return the isUserAnonymous
     */
    public boolean getIsUserAnonymous() {
        return isUserAnonymous;
    }

    /**
     * @param isUserAnonymous the isUserAnonymous to set
     */
    public void setIsUserAnonymous(boolean isUserAnonymous) {
        this.isUserAnonymous = isUserAnonymous;
    }

    /**
     * @return the anonymousName
     */
    public String getAnonymousName() {
        return anonymousName;
    }

    /**
     * @param anonymousName the anonymousName to set
     */
    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Student[ id=" + id + " ]";
    }

    public List<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(List<PostLike> likes) {
        this.likes = likes;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
    
    
    

}
