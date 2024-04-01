/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Course;
import entity.Post;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author izlim
 */
@Stateless
public class CourseSessionBean implements CourseSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Course> searchCourses(String courseCode) {
        Query q;
        if (courseCode != null) {
            q = em.createQuery("SELECT c FROM Course c WHERE "
                    + "LOWER(c.courseCode) LIKE :courseCode");
            q.setParameter("courseCode", "%" + courseCode.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT c FROM Course c");
        }

        return q.getResultList();

    }
    
    @Override
    public Course getCourse(Long cId) throws NoResultException{
        Course c = em.find(Course.class, cId);
        if (c != null) {
            return c;
        } else {
            throw new NoResultException("Course not found!");
        }
    }
    
    @Override
    public void createCourse(Course c) {
        em.persist(c);
        em.flush();
    }
    
    @Override
    public void updateCourse(Course c) throws NoResultException {
        Query q;
        if (c.getCourseId() != null) {
            Course course = getCourse(c.getCourseId());
            course.setCourseCode(c.getCourseCode());
            course.setCourseName(c.getCourseName());
            course.setDescription(c.getDescription());
            course.setPosts(c.getPosts());
            em.merge(course);
        } else {
            throw new NoResultException("Course not found!");
        }
    }
    
    @Override
    public void deleteCourse(Long cId) throws NoResultException{
        Course course = em.find(Course.class, cId);
        for (Post p : course.getPosts()) {
            p.setCourse(null);
            em.remove(p);
        }
        em.remove(course);
        em.flush();
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
