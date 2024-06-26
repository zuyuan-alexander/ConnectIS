/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Course;
import entity.Post;
import entity.PostLike;
import entity.Student;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author alvintjw
 */
@Local
public interface StudentSessionBeanLocal {
    

    public Student retrieveStudentByEmail(String email) throws exception.NoResultException;

    public void createStudent(Student s);

    public Student getStudent(Long cId) throws exception.NoResultException;

    public void updateStudent(Student s)throws exception.NoResultException ;
    
    public Boolean checkIfEmailExists(String email);
    
    public Boolean checkIfContactExists(String contact);

    public void addPinnedCourse(Course course, Student student);

    public void removePinnedCourse(Course course, Student student);

    public List<Post> getCreatedPosts(Student s);

    public List<PostLike> getLikedPosts(Student s);

    public boolean checkPassword(Student s, String password);
    
}
