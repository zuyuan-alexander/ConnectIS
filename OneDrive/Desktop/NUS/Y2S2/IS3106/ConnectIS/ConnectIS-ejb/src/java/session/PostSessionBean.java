/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Course;
import entity.Post;
import entity.PostLike;
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

    @Override
    public void createPost(Post post, Long studentid, Long courseid) {
        Student s = em.find(Student.class, studentid);
        Course c = em.find(Course.class, courseid);
        post.setStudent(s);
        post.setCourse(c);
        c.getPosts().add(post);
        s.getPosts().add(post);
        em.merge(s);
        em.merge(c);
        em.persist(post);
    }

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
    public List<Post> searchPostsByTitle(String title) {
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.title LIKE :inTitle");
        query.setParameter("inTitle", "%" + title + "%");

        return query.getResultList();
    }

    @Override
    public List<Post> retrievePostByCourse(Course c) {
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.course.courseId =:courseId");
        query.setParameter("courseId", c.getCourseId());

        try {
            return (List<Post>) query.getResultList();
        } catch (NoResultException ex) {
            throw new NoResultException("Could not find Posts!");
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
        Post oldPost = findPostById(post.getId());
        oldPost.setTitle(post.getTitle());
        oldPost.setPostType(post.getPostType());
        oldPost.setContent(post.getContent());
        oldPost.setAnonymous(post.isAnonymous());
        em.merge(oldPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = em.find(Post.class, id);
        if (post != null) {
            Course c = post.getCourse();
            c.getPosts().remove(post);
            Student s = post.getStudent();
            s.getPosts().remove(post);
            em.merge(s);
            em.merge(s);
            em.remove(post);
        }
    }

    @Override
    public void likePost(Long studentId, Long postId) {
        Student student = em.find(Student.class, studentId);
        Post post = em.find(Post.class, postId);

        // Check if the student has already liked this post
        if (!hasStudentLikedPost(studentId, postId)) {
            PostLike like = new PostLike();
            like.setStudent(student);
            like.setPost(post);
            student.getLikes().add(like);
            em.merge(student);
            em.persist(like);
        } else {
            Query query = em.createQuery("SELECT l FROM PostLike l WHERE l.student.id = :studentId AND l.post.id = :postId");
            query.setParameter("studentId", student.getId());
            query.setParameter("postId", post.getId());
            PostLike selectedPostLike = (PostLike) query.getSingleResult();
            Student s = em.find(Student.class, selectedPostLike.getStudent().getId());
            s.getLikes().remove(selectedPostLike);
            em.merge(s);
            em.remove(selectedPostLike);

        }
    }

    @Override
    public boolean hasStudentLikedPost(Long studentId, Long postId) {
        Student student = em.find(Student.class, studentId);
        Post post = em.find(Post.class, postId);

        Long likesCount = (Long) em.createQuery(
                "SELECT COUNT(l) FROM PostLike l WHERE l.student.id = :studentId AND l.post.id = :postId")
                .setParameter("studentId", student.getId())
                .setParameter("postId", post.getId())
                .getSingleResult();
        return likesCount > 0;
    }

    @Override
    public Long getLikesCount(Long postId) {
        Long likesCount = (Long) em.createQuery(
                "SELECT COUNT(l) FROM PostLike l WHERE l.post.id = :postId")
                .setParameter("postId", postId)
                .getSingleResult();
        return likesCount;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
