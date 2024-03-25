/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Comment;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Inject;
import session.CommentSessionBeanLocal;
import session.PostSessionBeanLocal;
import session.StudentSessionBeanLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "commentManagedBean")
@ViewScoped
public class CommentManagedBean implements Serializable {

    @EJB
    private CommentSessionBeanLocal commentSessionBean;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    @Inject
    private AuthenticationManagedBean authenBean;

    @Inject
    private PostManagedBean postBean;

    private String content;
    private boolean anonymous;

    /**
     * Creates a new instance of CommentManagedBean
     */
    public CommentManagedBean() {
    }

    public void likeComment(Long commentId) {
        System.out.println("Like Comment is called");
        Comment comment = commentSessionBean.getComment(commentId); // Implement this method to retrieve the comment by ID
        comment.setLikes(comment.getLikes() + 1);
        commentSessionBean.updateComment(comment); // Implement this method to update the comment in the database
    }

    public void dislikeComment(Long commentId) {
         System.out.println("Dislike Comment is called");
        Comment comment = commentSessionBean.getComment(commentId); // Implement this method to retrieve the comment by ID
        comment.setDislikes(comment.getDislikes()+ 1);
        commentSessionBean.updateComment(comment); // Implement this method to update the comment in the database
    }
    
     public void test() {
        System.out.println("Test Comment is called");
     }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

}
