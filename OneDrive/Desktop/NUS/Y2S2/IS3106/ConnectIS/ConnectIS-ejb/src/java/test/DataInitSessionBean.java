/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package test;

import entity.Comment;
import entity.Post;
import entity.Student;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import session.CommentSessionBeanLocal;
import session.PostSessionBeanLocal;
import session.StudentSessionBeanLocal;
import util.enumeration.PostTypeEnum;

/**
 *
 * @author alvintjw
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean{

    @EJB
    private CommentSessionBeanLocal commentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;
    
    
    
    

    @PersistenceContext(unitName = "ConnectIS-ejbPU")
    private EntityManager em;
    
 
    @PostConstruct
    public void postConstruct() {
        Student s = new Student();
        s.setFirstname("Alvin");
        s.setEmail("Alvin");
        
        studentSessionBean.createStudent(s);
        Student ss = studentSessionBean.retrieveStudentByEmail("Alvin");
        Post temp = new Post();
        String title = "Should I SU IS3106 :(";
        String content = "Yes you definitely should.";
        PostTypeEnum postType = PostTypeEnum.LECTURE;
        Date creationDate = new Date();
        boolean anonymous = true;
       
        temp.setAnonymous(anonymous);
        temp.setContent(content);
        temp.setTitle(title);
        temp.setCreationDate(creationDate);
        temp.setPostType(postType);
        
        postSessionBean.createPost(temp, ss);
        Post pp =  postSessionBean.retrievePostByTitle("Should I SU IS3106 :(");
        Comment comment1 = new Comment("Comment 1", true);
        Comment comment2 = new Comment("Comment 2", false);
        Comment comment3 = new Comment("Comment 3", true);
        Comment comment4 = new Comment("I want to SU A-", false);
        Comment comment5 = new Comment("Prof Lek Please help me", true);
        
        commentSessionBean.createComment(comment1, pp.getId(), ss.getId());
        commentSessionBean.createComment(comment2, pp.getId(), ss.getId());
        commentSessionBean.createComment(comment3, pp.getId(), ss.getId());
          commentSessionBean.createComment(comment4, pp.getId(), ss.getId());
        commentSessionBean.createComment(comment5, pp.getId(), ss.getId());
        
        

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
