 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Course;
import entity.Student;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import exception.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author alvintjw
 */
@Stateless
public class StudentSessionBean implements StudentSessionBeanLocal {

    @PersistenceContext(unitName = "ConnectIS-ejbPU")
    private EntityManager em;

   

    @Override
    public void createStudent(Student s) {
          s.setProfilePicture("dummy.jpeg");
        em.persist(s);
    }

    @Override
    public Student getStudent(Long cId) throws exception.NoResultException{
        Student stu = em.find(Student.class, cId);

        if (stu != null) {
            return stu;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public Student retrieveStudentByEmail(String email) throws exception.NoResultException {
        Query query = em.createQuery("SELECT s FROM Student s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (Student) query.getSingleResult();
        } catch (javax.persistence.NoResultException | NonUniqueResultException ex) {
            throw new NoResultException("Student email " + email + " does not exist!");
        }
    }

     @Override
    public void updateStudent(Student s) throws exception.NoResultException {
        Student oldStudent = getStudent(s.getId());
        oldStudent.setProfilePicture(s.getProfilePicture());
        oldStudent.setFirstname(s.getFirstname());
        oldStudent.setLastname(s.getLastname());
        oldStudent.setGender(s.getGender());
        oldStudent.setDob(s.getDob());
        oldStudent.setContactnumber(s.getContactnumber());
        oldStudent.setEmail(s.getEmail());
        oldStudent.setPassword(s.getPassword());
        oldStudent.setAcademicYear(s.getAcademicYear());
        oldStudent.setDegree(s.getDegree());
        oldStudent.setSpecialization(s.getSpecialization());
        oldStudent.setIsUserAnonymous(s.getIsUserAnonymous());
        oldStudent.setAnonymousName(s.getAnonymousName());
    }

    @Override
    public Boolean checkIfEmailExists(String email) {
        Query q = em.createQuery("SELECT COUNT(s) FROM Student s WHERE p.email = :inEmail");
        q.setParameter("inEmail", email);
        Long count = (Long) q.getSingleResult();
        return count > 0;
    }
    
    @Override
    public void addPinnedCourse(Course course, Student student) {
        Course c = em.find(Course.class, course.getCourseId());
        Student s = em.find(Student.class, student.getId());
        if (c != null && s != null) {
            s.getPinnedCourses().add(c);
            em.merge(s);
        }
    }

    @Override
    public Boolean checkIfContactExists(String contact) {
        Query q = em.createQuery("SELECT COUNT(s) FROM Student s WHERE s.contact = :inContact");
        q.setParameter("inContact", contact);
        Long count = (Long) q.getSingleResult();
        return count > 0;
    }

}
