/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Course;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.Lob;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import session.CourseSessionBeanLocal;
import session.StudentSessionBeanLocal;

/**
 *
 * @author izlim
 */
@Named(value = "courseManagedBean")
@ViewScoped
public class CourseManagedBean implements Serializable {

    @EJB
    private CourseSessionBeanLocal courseSessionBeanLocal;
    
    @EJB
    private StudentSessionBeanLocal studentSessionBeanLocal;

    private Long courseId;
    private String courseCode;
    private String courseName;
    @Lob
    private String description;
    private String semester;

    private String searchString;

    private Course selectedCourse;

    private List<Course> pinnedCourses;
    private List<Course> filteredCourses;
    private List<Course> allCourses;

    public CourseManagedBean() {
    }

    @PostConstruct
    public void init() {

        //Update Logged In Person using ID
        allCourses = courseSessionBeanLocal.searchCourses(null);
        filteredCourses = new ArrayList<>();
        List<Course> filteredCourses = courseSessionBeanLocal.searchCourses(searchString);

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String courseIdParam = params.get("courseId");
        if (courseIdParam != null) {
            Long courseId = Long.valueOf(courseIdParam);
            try {
                selectedCourse = courseSessionBeanLocal.getCourse(courseId);
                loadSelectedCourse();
            } catch (NoResultException ex) {
                Logger.getLogger(CourseManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        loadPinnedCourses();
    }

    public void handleSearch() {
        init();
    }

    public String viewCourseDetails(Course course) {
        return "courseHomePage?courseId=" + course.getCourseId() + "&faces-redirect=true";

    }

    public void loadSelectedCourse() {
        this.courseId = selectedCourse.getCourseId();
        this.courseCode = selectedCourse.getCourseCode();
        this.courseName = selectedCourse.getCourseName();
        this.description = selectedCourse.getDescription();
        this.semester = selectedCourse.getSemester();
    }

    public void loadPinnedCourses() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();

        try {
            Long userId = (Long) session.getAttribute("userId");
            Student s = studentSessionBeanLocal.getStudent(userId);
            this.pinnedCourses = s.getPinnedCourses();
            for (Course c : this.pinnedCourses) {
                System.out.println("Pinned Course: " + c.getCourseCode());
            }
        } catch (exception.NoResultException ex) {
            ex.getMessage();
        }
    }

    public String getCourseLinkClass(Long courseId) {
        // Obtain the current instance of FacesContext
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Get the HttpServletRequest from the FacesContext
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        // Retrieve the request URI and the query string
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();  // Correct method to get the query string

        // Construct the full request URL with the query string
        String fullRequestURI = requestURI + (queryString != null ? "?" + queryString : "");

        // Check if the constructed URI matches the expected URI for an active course
        return fullRequestURI.equals("/ConnectIS-war/courseHomePage.xhtml?courseId=" + courseId) ? "active" : "";
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public List<Course> getFilteredCourses() {
        return filteredCourses;
    }

    public void setFilteredCourses(List<Course> filteredCourses) {
        this.filteredCourses = filteredCourses;
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(List<Course> allCourses) {
        this.allCourses = allCourses;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Course> getPinnedCourses() {
        return pinnedCourses;
    }

    public void setPinnedCourses(List<Course> pinnedCourses) {
        this.pinnedCourses = pinnedCourses;
    }

}
