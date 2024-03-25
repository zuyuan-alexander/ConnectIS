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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import session.PostSessionBeanLocal;
import session.StudentSessionBeanLocal;
import util.enumeration.PostTypeEnum;

/**
 *
 * @author alvintjw
 */
@Named(value = "postManagedBean")
@ViewScoped
public class PostManagedBean implements Serializable {

    @EJB
    private StudentSessionBeanLocal studentSessionBean;
    
    @EJB
    private PostSessionBeanLocal postSessionBean;
    
    
    private String title;
    private String content;
    private PostTypeEnum postType;
    private boolean anonymous;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    private List<Comment> comments;
    private Student student;
    private Post selectedPost;

    /**
     * Creates a new instance of PostManagedBean
     */
    public PostManagedBean() {
    }
    
    @PostConstruct
    public void testInit() {
        //Initialise the post
        Post temp = new Post();
        title = "Should I SU IS3106 :(";
        content = "Yes you definitely should";
        postType = PostTypeEnum.LECTURE;
        creationDate = new Date();
        anonymous = true;
       
        temp.setAnonymous(anonymous);
        temp.setContent(content);
        temp.setTitle(title);
        temp.setCreationDate(creationDate);
        temp.setPostType(postType);
        
        selectedPost = temp;
        student = studentSessionBean.retrieveStudentByEmail("Alvin");
        
       
//        postSessionBean.createPost(selectedPost, student);
        loadSelectedPost();
        
        //Initialise the comments

    }
    
    public void addPost(ActionEvent evt) {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setCreationDate(creationDate);
        p.setPostType(postType);
        p.setAnonymous(anonymous);
    } //end addCustomer
    
    public void loadSelectedPost() {
        selectedPost = postSessionBean.retrievePostByTitle("Should I SU IS3106 :(");
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public PostTypeEnum getPostType() {
        return postType;
    }
    
    public void setPostType(PostTypeEnum postType) {
        this.postType = postType;
    }
    
    public boolean isAnonymous() {
        return anonymous;
    }
    
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }
    
}
