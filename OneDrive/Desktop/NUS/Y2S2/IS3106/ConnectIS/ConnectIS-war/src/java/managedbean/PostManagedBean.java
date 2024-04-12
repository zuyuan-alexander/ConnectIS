/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Comment;
import entity.Course;
import entity.Post;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import session.CourseSessionBeanLocal;
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

    @EJB
    private CourseSessionBeanLocal courseSessionBean;

    @Inject
    private AuthenticationManagedBean authenBean;

    private String title;
    private String content;
    private String postType;
    private boolean anonymous;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    private List<Comment> comments;
    private Student loggedinStudent;
    private Long selectedPostId;
    private Post selectedPost;
    private List<Post> posts;

    private Course selectedCourse;
    private List<Post> postsInSelectedCourse;

    /**
     * Creates a new instance of PostManagedBean
     */
    public PostManagedBean() {
    }

    @PostConstruct
    public void testInit() {
        posts = postSessionBean.getAllPosts();
        loggedinStudent = authenBean.getLoggedinStudent();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String courseIdParam = params.get("courseId");
        if (courseIdParam != null) {
            Long courseId = Long.valueOf(courseIdParam);
            try {
                selectedCourse = courseSessionBean.getCourse(courseId);
                postsInSelectedCourse = postSessionBean.retrievePostByCourse(selectedCourse);
            } catch (NoResultException ex) {
                Logger.getLogger(PostManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Initialise the post
//        Post temp = new Post();
//        title = "Should I SU IS3106 :(";
//        content = "Yes you definitely should";
//        postType = PostTypeEnum.LECTURE;
//        creationDate = new Date();
//        anonymous = true;
//       
//        temp.setAnonymous(anonymous);
//        temp.setContent(content);
//        temp.setTitle(title);
//        temp.setCreationDate(creationDate);
//        temp.setPostType(postType);
//        
//        selectedPost = temp;
//        student = studentSessionBean.retrieveStudentByEmail("Alvin");
//        
//       
////        postSessionBean.createPost(selectedPost, student);
//        loadSelectedPost();
        //Initialise the comments
    }

    public void addPost() {
        System.out.println("Triggering add post");
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setCreationDate(new Date());
        p.setAnonymous(anonymous);

        if (postType.equalsIgnoreCase("lecture")) {
            p.setPostType(PostTypeEnum.LECTURE);
        } else if (postType.equalsIgnoreCase("lab")) {
            p.setPostType(PostTypeEnum.LAB);
        } else if (postType.equalsIgnoreCase("tutorial")) {
            p.setPostType(PostTypeEnum.TUTORIAL);
        } else if (postType.equalsIgnoreCase("project")) {
            p.setPostType(PostTypeEnum.PROJECT);
        } else if (postType.equalsIgnoreCase("others")) {
            p.setPostType(PostTypeEnum.OTHERS);
        }
        postSessionBean.createPost(p);
    } //end addCustomer

    public void loadSelectedPost() {
        if(selectedPostId != null) {
            selectedPost = postSessionBean.findPostById(selectedPostId);
        }      
    }

    public Long getLikesCount(Long PostId) {
        
        return postSessionBean.getLikesCount(PostId);
    }

    public boolean isPostLikedByStudent(Long postId) {
        // Check if the post is liked by the student.
        return postSessionBean.hasStudentLikedPost(loggedinStudent.getId(), postId);
    }

    public void likePost(Long postId) {
        
        postSessionBean.likePost(loggedinStudent.getId(), postId);
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
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
        return loggedinStudent;
    }

    public void setStudent(Student student) {
        this.loggedinStudent = student;
    }

    public Student getLoggedinStudent() {
        return loggedinStudent;
    }

    public void setLoggedinStudent(Student loggedinStudent) {
        this.loggedinStudent = loggedinStudent;
    }

    public Long getSelectedPostId() {
        return selectedPostId;
    }

    public void setSelectedPostId(Long selectedPostId) {
        this.selectedPostId = selectedPostId;
    }

   
    
    
    public Post getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public List<Post> getPostsInSelectedCourse() {
        return postsInSelectedCourse;
    }

    public void setPostsInSelectedCourse(List<Post> postsInSelectedCourse) {
        this.postsInSelectedCourse = postsInSelectedCourse;
    }

}
