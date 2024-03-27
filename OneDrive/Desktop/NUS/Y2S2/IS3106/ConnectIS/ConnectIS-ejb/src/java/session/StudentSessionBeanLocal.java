/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Student;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author alvintjw
 */
@Local
public interface StudentSessionBeanLocal {
    

    public Student retrieveStudentByEmail(String email) throws NoResultException;

    public void createStudent(Student s);

    public Student getStudent(Long cId) throws NoResultException;

    public void updateStudent(Student s);
    
    public Boolean checkIfEmailExists(String email);
    
    public Boolean checkIfContactExists(String contact);
    
}
