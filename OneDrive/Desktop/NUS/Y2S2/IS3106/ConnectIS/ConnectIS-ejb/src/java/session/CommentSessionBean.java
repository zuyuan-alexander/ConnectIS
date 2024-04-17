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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public void createComment(Comment comment, Long parentcommentid, Long postid, Long studentid) {

        Comment parentComment = em.find(Comment.class, parentcommentid);
        System.out.println("Parent comment is: " + parentComment.getContent());
        System.out.println("Nested comment is: " + comment.getContent());
        parentComment.getReplies().add(comment);
        comment.setParentComment(parentComment);
        Post p = em.find(Post.class, postid);
        Student s = em.find(Student.class, studentid);
        comment.setPost(p);
        comment.setStudent(s);

        em.persist(comment);
    }

    @Override
    public Comment retrieveAnyComment() throws NoResultException {
        try {
            return em.createQuery("SELECT c FROM Comment c", Comment.class).getResultList().get(1);

        } catch (NoResultException ex) {
            throw new NoResultException("No comments exist in the database.");
        }
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
        //System.out.println("Delete session bean is called with id: " + id);
        Comment comment = getComment(id);
        if (comment != null) {
            //System.out.println("Comment is not null");
          
            Post p = comment.getPost();
            if(p != null) {
                p.getComments().remove(comment);
            }
            // Remove the comment from its parent's list of replies
            Comment parentComment = comment.getParentComment();
            if (parentComment != null) {
                parentComment.getReplies().remove(comment);       
            }

            em.remove(comment);
        } else {
            //System.out.println("Comment is null");
        }

    }

    @Override
    public List<Comment> findAllCommentsByPost(Long postId) {
        TypedQuery<Comment> query = em.createQuery(
                "SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    @Override
    public List<Comment> findCommentsWithoutReplies(Long postId) {
        return em.createQuery(
                "SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL AND c.replies IS EMPTY", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
