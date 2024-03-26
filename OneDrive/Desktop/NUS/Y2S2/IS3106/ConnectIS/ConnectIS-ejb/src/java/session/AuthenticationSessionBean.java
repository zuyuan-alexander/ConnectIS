/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Student;
import exception.InvalidLoginCredentialsException;
import exception.StudentExistsException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author erin
 */
@Stateless
public class AuthenticationSessionBean implements AuthenticationSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @Override
    public Long authenticatePerson(String email, String password) throws InvalidLoginCredentialsException {
        Query q = em.createQuery("SELECT s FROM Student s WHERE s.email =:inEmail AND s.password = :inStudent");
        q.setParameter("inEmail", email);
        q.setParameter("inPassword", password);
        Student s = (Student) q.getSingleResult();
        if (s.getId() != null) {
            return s.getId();
        } else {
            throw new InvalidLoginCredentialsException("Invalid email or password!");
        }
    }

    @Override
    public boolean registerStudent(String email, String contactnumber, String firstname, String lastname, String password) throws StudentExistsException {
//        Boolean emailExists = studentSessionBean.checkIfEmailExists(email);
//        Boolean contactExists = studentSessionBean.checkIfContactExists(contact);
//        
//        if (emailExists) {
//            throw new StudentExistsException("Email already exists!");
//        } else if (contactExists) {
//            throw new StudentExistsException("Contact already exists!");
//        }

        Student newStudent = new Student(email, contactnumber, firstname, lastname, password);
        studentSessionBean.createStudent(newStudent);
        System.out.println("Student created!");
        return true;

//        try {
//            System.out.println("Creating New Student");
//            Student newStudent = new Student();
//            newStudent.setFirstname(firstname);
//            newStudent.setLastname(lastname);
//            newStudent.setEmail(email);
//            newStudent.setContactnumber(contact);
//            newStudent.setPassword(password);
//            studentSessionBean.createStudent(newStudent);
//            System.out.println("Student created!");
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }

    }

}
