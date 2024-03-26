/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import exception.InvalidLoginCredentialsException;
import exception.StudentExistsException;
import javax.ejb.Local;

/**
 *
 * @author erin
 */
@Local
public interface AuthenticationSessionBeanLocal {
    
    public Long authenticatePerson(String email, String password) throws InvalidLoginCredentialsException;

    public boolean registerStudent(String email, String firstname, String lastname, String contactnumber, String password) throws StudentExistsException;
}
