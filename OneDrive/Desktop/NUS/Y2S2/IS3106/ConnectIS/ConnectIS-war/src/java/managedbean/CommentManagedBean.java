/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Comment;
import entity.Post;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
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

    private Post selectedPost;
    private Student loggedinStudent;
    private String content;
    private boolean anonymous;

    private Comment newReply; // This should be initialized when a new comment is prepared
    private Comment selectedComment;

    /**
     * Creates a new instance of CommentManagedBean
     */
    public CommentManagedBean() {
    }

    @PostConstruct
    public void init() {
        selectedPost = postBean.getSelectedPost();
        loggedinStudent = authenBean.getLoggedinStudent();

        newReply = new Comment(); // Initialize the new reply comment
    }


    public void replyToComment(Long commentId) {
        if (newReply.getContent() != null || !newReply.getContent().isEmpty()) {
            commentSessionBean.createComment(newReply, commentId, selectedPost.getId(), loggedinStudent.getId());
            newReply = new Comment();
        }
    }

    public String testComment(Long commentId) {
        System.out.println("This is being called");
        Comment comment6 = new Comment("Nested Comment Test", true);
        selectedComment = commentSessionBean.getComment(commentId);
        newReply.setContent(this.content);
        newReply.setAnonymous(this.anonymous);
        if (newReply.getContent() != null || !newReply.getContent().isEmpty()) {
            commentSessionBean.createComment(comment6, commentId, selectedPost.getId(), loggedinStudent.getId());
            newReply = new Comment();
        }

        setContent(null);
        setAnonymous(false);
        return null;
    }

    public String addComment() {
        newReply.setContent(this.content);
        newReply.setAnonymous(this.anonymous);
        if (newReply.getContent() != null || !newReply.getContent().isEmpty()) {
            commentSessionBean.createComment(newReply, selectedPost.getId(), loggedinStudent.getId());
            newReply = new Comment();
        }

        setContent(null);
        setAnonymous(false);
        return null;
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

    public Comment getNewReply() {
        return newReply;
    }

    public void setNewReply(Comment newReply) {
        this.newReply = newReply;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

}
