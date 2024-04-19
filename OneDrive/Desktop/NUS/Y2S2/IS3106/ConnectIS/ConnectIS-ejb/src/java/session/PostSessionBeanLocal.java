/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Comment;
import entity.Course;
import entity.Post;
import entity.Student;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author alvintjw
 */
@Local
public interface PostSessionBeanLocal {

    public void createPost(Post post, Student student);

    public Post findPostById(Long id);

    public List<Post> getAllPosts();

    public void updatePost(Post post);

    public void deletePost(Long id);

    public Post retrievePostByTitle(String title) throws NoResultException;

    public void createPost(Post post);

    public Long getLikesCount(Long postId);

    public void likePost(Long studentId, Long postId);

    public boolean hasStudentLikedPost(Long studentId, Long postId);

    public List<Post> retrievePostByCourse(Course c);

    public void createPost(Post post, Long studentid, Long courseid);

    public List<Post> searchPostsByTitle(String title);
    

}
