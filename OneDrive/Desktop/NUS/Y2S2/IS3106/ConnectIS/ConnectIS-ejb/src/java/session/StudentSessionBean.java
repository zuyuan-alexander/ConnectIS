/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Student;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        em.persist(s);
    }

    @Override
    public Student getStudent(Long cId) throws NoResultException {
        Student stu = em.find(Student.class, cId);

        if (stu != null) {
            return stu;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public Student retrieveStudentByEmail(String email) throws NoResultException{
        Query query = em.createQuery("SELECT s FROM Student s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (Student) query.getSingleResult();
        } catch (javax.persistence.NoResultException | NonUniqueResultException ex) {
            throw new NoResultException("Student email " + email + " does not exist!");
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
