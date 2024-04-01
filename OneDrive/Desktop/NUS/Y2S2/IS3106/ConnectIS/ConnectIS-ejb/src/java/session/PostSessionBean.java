/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Post;
import entity.Student;
import java.util.List;
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
public class PostSessionBean implements PostSessionBeanLocal {

    @PersistenceContext(unitName = "ConnectIS-ejbPU")
    private EntityManager em;

    @Override
    public void createPost(Post post, Student student) {
        Student s = em.find(Student.class, student.getId());
        post.setStudent(s);
        s.getPosts().add(post);
        em.persist(post);
    }

    @Override
    public void createPost(Post post) {
        em.persist(post);
    }
//   
//    @Override
//    public void createPost(Post post, Long studentid, Long courseid) {
//        Student s = em.find(Student.class, studentid);
//        Course c = em.find(Course.class, courseid);
//        post.setStudent(s);
//        post.setCourse(c);
//        c.getPost().add(post);
//        s.getPosts().add(post);
//        em.persist(post);
//    }

    @Override
    public Post findPostById(Long id) {
        return em.find(Post.class, id);
    }

    @Override
    public Post retrievePostByTitle(String title) throws NoResultException {
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.title = :inTitle");
        query.setParameter("inTitle", title);

        try {
            return (Post) query.getSingleResult();
        } catch (javax.persistence.NoResultException | NonUniqueResultException ex) {
            throw new NoResultException("Post title " + title + " does not exist!");
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    }

//    @Override
//    public List<Post> getAllPosts(Long courseid) {
//        return em.createQuery("SELECT p FROM Post p WHERE p.course.id = :courseID")
//                .setParameter(":courseID", courseid)
//                .getResultList();
//    }
    @Override
    public void updatePost(Post post) {
        em.merge(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = findPostById(id);
        if (post != null) {
            em.remove(post);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
