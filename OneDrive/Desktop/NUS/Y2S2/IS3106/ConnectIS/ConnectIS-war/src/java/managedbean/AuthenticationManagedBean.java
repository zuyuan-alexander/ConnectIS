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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import session.StudentSessionBeanLocal;
import session.AuthenticationSessionBeanLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private AuthenticationSessionBeanLocal authentificationSessionBean;

    private String email;
    private String password;
    private Long userId = -1l;
    private Student loggedinStudent;

    public AuthenticationManagedBean() {
    }

    //method to simulate using the logged in userId to generate user profile
    public String generateUserProfileData() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        setUserId((Long) session.getAttribute("userId"));
        //normally we will then use the userId to grab the user object from the session bean
        //but here we will simulate as if we did it

        String userProfile = "UserId: " + getUserId();
        return userProfile;
    }

    public String login() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        try {
            Student student = studentSessionBean.retrieveStudentByEmail(email);

            if (password.equals(student.getPassword())) {
                setUserId(student.getId());
                session.setAttribute("userId", getUserId());
                setLoggedinStudent(student);
                return "/homePage.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage("loginForm:password",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Login", "Incorrect Password! Please check your password."));
                return null;
            }
        } catch (exception.NoResultException ex) {
            FacesContext.getCurrentInstance().addMessage("loginForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Login", "Email not found in our database! Please check your email."));
            setUserId(-1l);
            session.setAttribute("userId", getUserId());
            email = null;
            password = null;
            return null;
        }
    }

    public String logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        setUserId(-1l);
        session.setAttribute("userId", getUserId());
        return "/login.xhtml?faces-redirect=true";
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

    public Student getLoggedinStudent() {
        return loggedinStudent;
    }

    public void setLoggedinStudent(Student loggedinStudent) {
        this.loggedinStudent = loggedinStudent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
