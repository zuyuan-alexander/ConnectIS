/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Student;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.persistence.NoResultException;
import session.StudentSessionBeanLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;
    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;
    private String password;
    private String confirmpassword;
    private Student loggedinStudent;
    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {
    }
    
     public String register() {
        
        if (!password.equals(confirmpassword)) {
            System.out.println("password is: " + password);
            System.out.println("confirmpassword is: " + confirmpassword);
            
            // Add error message about passwords not matching
            return "signup";
        }
        
        // Create and persist the new user entity
        Student newStudent = new Student();
        newStudent.setEmail(email);
        newStudent.setContactnumber(contactnumber);
        newStudent.setFirstname(firstname);
        newStudent.setLastname(lastname);
        newStudent.setPassword(password);
        studentSessionBean.createStudent(newStudent);        
        return "login.xhtml"; // Redirect to login page on successful registration
    }

    public String login() {
        
        try {
            loggedinStudent = studentSessionBean.retrieveStudentByEmail(email);
        } catch (NoResultException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        if (loggedinStudent != null && loggedinStudent.getPassword().equals(this.getPassword())) {
            //login successful
            //store the logged in user id
            //setUserId(10);
            //do redirect
            return "index.xhtml?faces-redict=true";
            //return "/secret/secret.xhtml?faces-redirect=true";
        } else {
            //login failed
            setEmail(null);
            setPassword(null);
            //setUserId(-1);
            return "login.xhtml";
        }
    }

    public String logout() {
        setEmail(null);
        setPassword(null);
        //setUserId(-1);
        return "/login.xhtml?faces-redirect=true";
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

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    
}
