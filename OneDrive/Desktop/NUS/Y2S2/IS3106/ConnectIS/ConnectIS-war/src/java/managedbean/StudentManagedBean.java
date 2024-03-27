/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import session.StudentSessionBeanLocal;

/**
 *
 * @author zuyua
 */
@Named(value = "studentManagedBean")
@ViewScoped
public class StudentManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;
    
    private Long id;
    
    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;
    private String password;
    
    private byte degree;
    private byte gender;
    private int academicYear;
    private Date dob;
    private String specialization;
    private boolean isUserAnonymous;
    private String anonymousName;
    
    private Student selectedStudent;
    
    public StudentManagedBean() {
    }
    
    public void createStudent(ActionEvent evt) {
        Student newStudent = new Student();
        newStudent.setEmail(email);
        newStudent.setContactnumber(contactnumber);
        newStudent.setFirstname(firstname);
        newStudent.setLastname(lastname);
        newStudent.setDegree(degree);
        newStudent.setGender(gender);
        newStudent.setAcademicYear(academicYear);
        newStudent.setDob(dob);
        newStudent.setSpecialization(specialization);
        newStudent.setIsUserAnonymous(false);
        newStudent.setAnonymousName("");
        studentSessionBeanLocal.createStudent(newStudent);
    }
    
    public void loadSelectedStudent() {
        /*
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        Long studentId = (Long) session.getAttribute("studentId");
        */
        Student temp = studentSessionBeanLocal.retrieveStudentByEmail("user1@u.nus.edu");
        selectedStudent = studentSessionBeanLocal.getStudent(temp.getId());
        
        setId(selectedStudent.getId());
        setEmail(selectedStudent.getEmail());
        setContactnumber(selectedStudent.getContactnumber());
        setFirstname(selectedStudent.getFirstname());
        setLastname(selectedStudent.getLastname());
        setPassword(selectedStudent.getPassword());
        setDegree(selectedStudent.getDegree());
        setGender(selectedStudent.getGender());
        setAcademicYear(selectedStudent.getAcademicYear());
        setDob(selectedStudent.getDob());
        setSpecialization(selectedStudent.getSpecialization());
        setIsUserAnonymous(selectedStudent.getIsUserAnonymous());
        setAnonymousName(selectedStudent.getAnonymousName());
    }
    
    public void updateStudent(ActionEvent evt) {
        selectedStudent.setContactnumber(getContactnumber());
        selectedStudent.setFirstname(getFirstname());
        selectedStudent.setLastname(getLastname());
        selectedStudent.setDegree(getDegree());
        selectedStudent.setGender(getGender());
        selectedStudent.setAcademicYear(getAcademicYear());
        selectedStudent.setDob(getDob());
        selectedStudent.setSpecialization(getSpecialization());
        selectedStudent.setIsUserAnonymous(getIsUserAnonymous());
        selectedStudent.setAnonymousName(getAnonymousName());
        studentSessionBeanLocal.updateStudent(selectedStudent);
    }
    
    public String cancelAction(ActionEvent evt) {
        return "viewProfile.xhtml?faces-redirect=true";
    }
    
    // getter and setter
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

    public boolean getIsUserAnonymous() {
        return isUserAnonymous;
    }

    public void setIsUserAnonymous(boolean isUserAnonymous) {
        this.isUserAnonymous = isUserAnonymous;
    }

    public String getAnonymousName() {
        return anonymousName;
    }

    public void setAnonymousName(String anonymousName) {
        this.anonymousName = anonymousName;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }
    
}
