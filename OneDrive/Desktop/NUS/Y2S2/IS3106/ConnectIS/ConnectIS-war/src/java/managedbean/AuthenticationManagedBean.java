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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import session.StudentSessionBeanLocal;
import session.AuthenticationSessionBeanLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "authentificationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;
    private AuthenticationSessionBeanLocal authentificationSessionBean;
    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;
    private String password;
    private String confirmpassword;
    private Student loggedinStudent;
    
    public AuthenticationManagedBean() {
    }
    
     public String signup() {
         
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            System.out.print("entering boolean success");
            System.out.print(email + " " + firstname + " " + lastname  + " " + contactnumber + " " + password);
            boolean success = authentificationSessionBean.registerStudent(email, firstname, lastname, contactnumber, password);
            System.out.print("teehee NOT SUCCESSFUL" + success);
            if (success) {
                context.addMessage(null, new FacesMessage("Successful sign-up!"));
                return "/login.xhtml?faces-redirect=true"; // Navigate to login page on success
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authentification Error", "Sign-up failed!"));
                return "signup.xhtml";
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception Error", "Sign-up failed!"));
            e.printStackTrace();
            return "signup.xhtml";
        }
//        if (!password.equals(confirmpassword)) {
//            System.out.println("password is: " + password);
//            System.out.println("confirmpassword is: " + confirmpassword);
//            
//            // Add error message about passwords not matching
//            return "signup";
//        }
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            loggedinStudent = studentSessionBean.retrieveStudentByEmail(email);
        } catch (NoResultException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        if (loggedinStudent != null && loggedinStudent.getEmail().equals(this.getEmail())) {
            return "index.xhtml?faces-redirect=true";
            //return "/secret/secret.xhtml?faces-redirect=true";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exception Error", "Login unsuccessful!"));
            return "login.xhtml";
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
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
