/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Comment;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author alvintjw
 */
@Local
public interface CommentSessionBeanLocal {

    public void createComment(Comment comment, Long postid, Long studentid);

    public Comment getComment(Long id);

    public void updateComment(Comment newComment);

    public void deleteComment(Long id);

    public List<Comment> findAllCommentsByPost(Long postId);
    
}
