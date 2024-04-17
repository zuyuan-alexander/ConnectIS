/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Student;
import exception.NoResultException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import session.StudentSessionBeanLocal;

/**
 *
 * @author zuyua
 */
@Named(value = "otherManagedBean")
@ViewScoped
public class OtherManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    private Long id;

    private Part uploadedFile;
    private String tempPicture;
    private String profilePicture;

    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;

    private byte degree;
    private byte gender;
    private int academicYear;
    private Date dob;
    private String specialization;

    private Student otherStudent;

    public OtherManagedBean() {
    }

    public void viewOtherUserProfile(Long studentId) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        session.setAttribute("otherStudentId", studentId);
    }

    public void loadOtherStudent() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        try {
            Long userId = (Long) session.getAttribute("otherStudentId");
            Student s = studentSessionBean.getStudent(userId);

            setOtherStudent(s);
            setFirstname(s.getFirstname());
            setLastname(s.getLastname());
            setProfilePicture(s.getProfilePicture());
            setContactnumber(s.getContactnumber());
            setEmail(s.getEmail());
            setDob(s.getDob());
            setGender(s.getGender());
            setAcademicYear(s.getAcademicYear());
            setDegree(s.getDegree());
            setSpecialization(s.getSpecialization());
        } catch (NoResultException ex) {
            ex.getMessage();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getTempPicture() {
        return tempPicture;
    }

    public void setTempPicture(String tempPicture) {
        this.tempPicture = tempPicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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

    public byte getDegree() {
        return degree;
    }

    public void setDegree(byte degree) {
        this.degree = degree;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Student getOtherStudent() {
        return otherStudent;
    }

    public void setOtherStudent(Student otherStudent) {
        this.otherStudent = otherStudent;
    }

}
