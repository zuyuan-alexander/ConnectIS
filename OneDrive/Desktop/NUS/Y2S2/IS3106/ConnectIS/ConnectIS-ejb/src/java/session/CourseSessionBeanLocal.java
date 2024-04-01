/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Course;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author izlim
 */
@Local
public interface CourseSessionBeanLocal {

    public List<Course> searchCourses(String courseName);

    public Course getCourse(Long cId) throws NoResultException;

    public void createCourse(Course c);

    public void updateCourse(Course c) throws NoResultException;

    public void deleteCourse(Long cId) throws NoResultException;
    
}
