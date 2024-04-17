/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Course;
import entity.Student;
import exception.NoResultException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import session.CourseSessionBeanLocal;
import session.StudentSessionBeanLocal;

/**
 *
 * @author zuyua original
 */
@Named(value = "studentManagedBean")
@ViewScoped
public class StudentManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private CourseSessionBeanLocal courseSessionBean;

    private Long id;

    private Part uploadedFile;
    private String tempPicture;
    private String profilePicture;

    private String email;
    private String contactnumber;
    private String firstname;
    private String lastname;
    private String password;
    private String confirmPassword;
    private String newPassword;

    private byte degree;
    private byte gender;
    private int academicYear;
    private Date dob;
    private String specialization;
    private boolean isUserAnonymous;
    private String anonymousName;

    private Student selectedStudent;
    private Student otherStudent;

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
        studentSessionBean.createStudent(newStudent);
    }

    public String signup(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();
        Date today = new Date();
        if (!password.equals(confirmPassword)) {
            context.addMessage("signUpForm:password",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Password", "Password mismatch! Plesae ensure Password is the same as Confirm Password"));
            context.addMessage("signUpForm:confirmPassword",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Password", "Password mismatch! Plesae ensure Password is the same as Confirm Password"));
            return null;
        } else if (getDob().after(today)) {
            context.addMessage("signUpForm:inputDOB",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Date", "Date of Birth cannot be a future date!"));
            return null;
        } else {
            Student newStudent = new Student();

            newStudent.setFirstname(firstname);
            newStudent.setLastname(lastname);
            newStudent.setProfilePicture(profilePicture);
            newStudent.setContactnumber(contactnumber);
            newStudent.setEmail(email);
            newStudent.setDob(dob);
            newStudent.setGender(gender);
            newStudent.setPassword(password);
            newStudent.setAcademicYear(academicYear);
            newStudent.setDegree(degree);
            newStudent.setSpecialization(specialization);
            newStudent.setIsUserAnonymous(false);
            newStudent.setAnonymousName("");
            studentSessionBean.createStudent(newStudent);
            return "login.xhtml?faces-redirect=true";
        }
    }

    public void loadSelectedStudent() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        try {
            Long userId = (Long) session.getAttribute("userId");
            Student s = studentSessionBean.getStudent(userId);

            setSelectedStudent(s);
            setFirstname(s.getFirstname());
            setLastname(s.getLastname());
            setProfilePicture(s.getProfilePicture());
            setContactnumber(s.getContactnumber());
            setEmail(s.getEmail());
            setDob(s.getDob());
            setGender(s.getGender());
            setPassword(s.getPassword());
            setAcademicYear(s.getAcademicYear());
            setDegree(s.getDegree());
            setSpecialization(s.getSpecialization());
            setIsUserAnonymous(s.getIsUserAnonymous());
            setAnonymousName(s.getAnonymousName());
        } catch (NoResultException ex) {
            ex.getMessage();
        }
    }

    public void updateStudent(ActionEvent evt) {
        selectedStudent.setContactnumber(getContactnumber());
        selectedStudent.setProfilePicture(getProfilePicture());
        selectedStudent.setFirstname(getFirstname());
        selectedStudent.setLastname(getLastname());
        selectedStudent.setDegree(getDegree());
        selectedStudent.setGender(getGender());
        selectedStudent.setAcademicYear(getAcademicYear());
        selectedStudent.setDob(getDob());
        selectedStudent.setSpecialization(getSpecialization());
        selectedStudent.setIsUserAnonymous(getIsUserAnonymous());
        selectedStudent.setAnonymousName(getAnonymousName());
        try {
            studentSessionBean.updateStudent(selectedStudent);
        } catch (NoResultException ex) {
        }
    }

    public void upload() throws IOException {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        //get the deployment path
        String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
        System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

        //debug purposes
        setProfilePicture(Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString());
        System.out.println("filename: " + getProfilePicture());
        //---------------------

        //replace existing file
        Path path = Paths.get(UPLOAD_DIRECTORY + getProfilePicture());
        InputStream bytes = uploadedFile.getInputStream();
        Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);

        setTempPicture(getProfilePicture());
    }

    public String changeProfilePicture(ActionEvent evt) throws IOException {
        try {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            //get the deployment path
            String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
            System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

            //debug purposes
            setProfilePicture(Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString());
            System.out.println("filename: " + getProfilePicture());
            //---------------------

            //replace existing file
            Path path = Paths.get(UPLOAD_DIRECTORY + getProfilePicture());
            InputStream bytes = uploadedFile.getInputStream();
            Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);

            selectedStudent.setProfilePicture(getProfilePicture());
            updateStudent(evt);
            return "/viewProfile.xhtml?faces-redirect=true";
        } catch (NullPointerException ex) {
            FacesContext.getCurrentInstance().addMessage("profilePictureForm:profilePicture",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Invalid Change! Please upload a profile picture before submitting",
                            "Invalid Change! Please upload a profile picture before submitting"));
            return null;
        }
    }

    public String changePassword(ActionEvent evt) {
        if (getPassword().equals(selectedStudent.getPassword())) {
            if (getNewPassword().equals(getConfirmPassword())) {
                setPassword(getNewPassword());
                selectedStudent.setPassword(getNewPassword());
                try {
                    studentSessionBean.updateStudent(selectedStudent);
                    return "/viewProfile.xhtml?faces-redirect=true";
                } catch (NoResultException ex) {
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("myForm:newPassword",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Password", "Password mismatch! Please ensure New Password is same as Confirm Password"));
                FacesContext.getCurrentInstance().addMessage("myForm:confirmPassword",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Password", "Password mismatch! Please ensure New Password is same as Confirm Password"));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("myForm:oldPassword",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect password", "Incorrect old password! Please key in the correct password to proceed"));
            return null;
        }
    }

    public String cancelAction(ActionEvent evt) {
        return "viewProfile.xhtml?faces-redirect=true";
    }

    public void followCourse(Course c, Student s) {
        try {
            Course course = courseSessionBean.getCourse(c.getCourseId());
            Student student = studentSessionBean.getStudent(s.getId());
            if (course != null && student != null) {
                studentSessionBean.addPinnedCourse(course, student);
            }
        } catch (NoResultException ex) {
            Logger.getLogger(StudentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void unfollowCourse(Course c, Student s) {
        try {
            Course course = courseSessionBean.getCourse(c.getCourseId());
            Student student = studentSessionBean.getStudent(s.getId());
            if (course != null && student != null) {
                studentSessionBean.removePinnedCourse(course, student);
            }
        } catch (NoResultException ex) {
            Logger.getLogger(StudentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isCoursePinned(Course c, Student s) {
        try {
            Course course = courseSessionBean.getCourse(c.getCourseId());
            Student student = studentSessionBean.getStudent(s.getId());
            if (course != null && student != null && student.getPinnedCourses() != null) {
                for (Course cc : student.getPinnedCourses()) {
                    if (cc.getCourseId().equals(course.getCourseId())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NoResultException ex) {
            Logger.getLogger(StudentManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Student getOtherStudent() {
        return otherStudent;
    }

    public void setOtherStudent(Student otherStudent) {
        this.otherStudent = otherStudent;
    }

}
