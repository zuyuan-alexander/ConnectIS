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
import java.util.ArrayList;
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
import session.CommentSessionBeanLocal;
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
    private CommentSessionBeanLocal commentSessionBean;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    @EJB
    private CourseSessionBeanLocal courseSessionBean;

    @Inject
    private AuthenticationManagedBean authenBean;

    @Inject
    private StudentManagedBean studentManagedBean;

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
    private List<Post> filterPost;
    private List<Comment> filteredComment;

    private String searchString;
    private String searchType = "all";

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
                filterPost = postsInSelectedCourse;
            } catch (NoResultException ex) {
                Logger.getLogger(PostManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String postIdParam = params.get("postId");
        if (postIdParam != null) {
            Long postId = Long.valueOf(postIdParam);
            try {
                selectedPost = postSessionBean.findPostById(postId);
                loadSelectedPost();
            } catch (NoResultException ex) {
                Logger.getLogger(PostManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String addPost() {
        System.out.println("Triggering add post");
        Post p = new Post();
        if (postType.equalsIgnoreCase("lecture")) {
            p.setPostType(PostTypeEnum.LECTURE);
        } else if (postType.equalsIgnoreCase("labs")) {
            p.setPostType(PostTypeEnum.LAB);
        } else if (postType.equalsIgnoreCase("tutorials")) {
            p.setPostType(PostTypeEnum.TUTORIAL);
        } else if (postType.equalsIgnoreCase("project")) {
            p.setPostType(PostTypeEnum.PROJECT);
        } else if (postType.equalsIgnoreCase("others")) {
            p.setPostType(PostTypeEnum.OTHERS);
        }

        p.setTitle(title);
        p.setContent(content);
        p.setCreationDate(new Date());
        p.setAnonymous(anonymous);

        postSessionBean.createPost(p, loggedinStudent.getId(), selectedCourse.getCourseId());
        return "/courseHomePage.xhtml?courseId=" + selectedCourse.getCourseId() + "&faces-redirect=true";

    }

    public void loadSelectedPost() {
        if (selectedPost != null) {
            title = selectedPost.getTitle();
            content = selectedPost.getContent();
            anonymous = selectedPost.isAnonymous();

            if (selectedPost.getPostType().equals(PostTypeEnum.LECTURE)) {
                postType = "lecture";
            } else if (selectedPost.getPostType().equals(PostTypeEnum.LAB)) {
                postType = "labs";
            } else if (selectedPost.getPostType().equals(PostTypeEnum.TUTORIAL)) {
                postType = "tutorials";
            } else if (selectedPost.getPostType().equals(PostTypeEnum.PROJECT)) {
                postType = "project";
            } else if (selectedPost.getPostType().equals(PostTypeEnum.OTHERS)) {
                postType = "others";
            }
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
        studentManagedBean.loadSelectedStudent();
    }

    public String deletePost(Long postId) {
        Post p = postSessionBean.findPostById(postId);
        postSessionBean.deletePost(postId);
        return "/courseHomePage.xhtml?courseId=" + selectedCourse.getCourseId() + "&faces-redirect=true";
    }

    public int countComments(Long postId) {
        return commentSessionBean.findAllCommentsByPost(postId).size();
    }

    public String updatePost() {
        selectedPost.setTitle(this.getTitle());
        selectedPost.setContent(this.getContent());
        selectedPost.setAnonymous(this.isAnonymous());

        if (postType.equalsIgnoreCase("lecture")) {
            selectedPost.setPostType(PostTypeEnum.LECTURE);
        } else if (postType.equalsIgnoreCase("labs")) {
            selectedPost.setPostType(PostTypeEnum.LAB);
        } else if (postType.equalsIgnoreCase("tutorials")) {
            selectedPost.setPostType(PostTypeEnum.TUTORIAL);
        } else if (postType.equalsIgnoreCase("project")) {
            selectedPost.setPostType(PostTypeEnum.PROJECT);
        } else if (postType.equalsIgnoreCase("others")) {
            selectedPost.setPostType(PostTypeEnum.OTHERS);
        }

        postSessionBean.updatePost(selectedPost);
        return "/courseHomePage.xhtml?courseId=" + selectedCourse.getCourseId() + "&faces-redirect=true";
    }

    // Method to search and filter posts
    public void searchPosts() {
        System.out.println("Search String is: " + searchString);
        System.out.println("Search Type is: " + searchType);

        if (searchString.isEmpty() || searchString == null) {
            filterPost = postsInSelectedCourse;
            return;
        }
        List<Post> newfilterPost = new ArrayList<>();
        newfilterPost = postSessionBean.searchPostsByTitle(searchString);
        for (Post p : newfilterPost) {
            System.out.println("Post Title: " + p.getTitle());
        }

        if (newfilterPost.isEmpty()) {
            System.out.println("FilterPost should be empty");
        }
        filterPost = newfilterPost;

    }

    public void searchPostByType() {
        System.out.println("searchPostByType is called");

        List<Post> newfilterPost = new ArrayList<>();

        if ("all".equalsIgnoreCase(searchType)) {
            filterPost = postsInSelectedCourse;
            return; // Exit the method early if searchType is "all"
        }

        for (Post post : postsInSelectedCourse) {
            if (searchType.equalsIgnoreCase("lecture") && post.getPostType() == PostTypeEnum.LECTURE) {
                newfilterPost.add(post);
            } else if (searchType.equalsIgnoreCase("labs") && post.getPostType() == PostTypeEnum.LAB) {
                newfilterPost.add(post);
            } else if (searchType.equalsIgnoreCase("tutorials") && post.getPostType() == PostTypeEnum.TUTORIAL) {
                newfilterPost.add(post);
            } else if (searchType.equalsIgnoreCase("project") && post.getPostType() == PostTypeEnum.PROJECT) {
                newfilterPost.add(post);
            } else if (searchType.equalsIgnoreCase("others") && post.getPostType() == PostTypeEnum.OTHERS) {
                newfilterPost.add(post);
            }
        }

        filterPost = newfilterPost;

        for (Post p : newfilterPost) {
            System.out.println("Post Title: " + p.getTitle());
        }
//        return "/courseHomePage.xhtml?courseId=" + selectedCourse.getCourseId();
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

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public List<Post> getFilterPost() {
        return filterPost;
    }

    public void setFilterPost(List<Post> filterPost) {
        this.filterPost = filterPost;
    }

    public List<Comment> getFilteredComment() {
        return filteredComment;
    }

    public void setFilteredComment(List<Comment> filteredComment) {
        this.filteredComment = filteredComment;
    }

}
