/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Comment;
import entity.Post;
import entity.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author alvintjw
 */
@Stateless
public class CommentSessionBean implements CommentSessionBeanLocal {

    @PersistenceContext(unitName = "ConnectIS-ejbPU")
    private EntityManager em;
    
    @Override
    public void createComment(Comment comment, Long postid, Long studentid) {
        Post p = em.find(Post.class, postid);
        Student s = em.find(Student.class, studentid);
        comment.setPost(p);
        comment.setStudent(s);
        p.getComments().add(comment);
        em.persist(comment);
    }
    
    @Override
    public Comment getComment(Long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public void updateComment(Comment newComment) {
        Comment oldC = getComment(newComment.getId());
        oldC.setContent(newComment.getContent());
        oldC.setLikes(newComment.getLikes());
        oldC.setDislikes(newComment.getDislikes());
       
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = getComment(id);
        if (comment != null) {
            em.remove(comment);
        }
    }
    
    
      @Override
    public List<Comment> findAllCommentsByPost(Long postId) {
        TypedQuery<Comment> query = em.createQuery(
            "SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

   
}
