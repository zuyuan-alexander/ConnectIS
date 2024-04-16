/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package test;

import entity.Comment;
import entity.Course;
import entity.Post;
import entity.Student;
import exception.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import session.CommentSessionBeanLocal;
import session.CourseSessionBeanLocal;
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
public class DataInitSessionBean {

    @EJB
    private CommentSessionBeanLocal commentSessionBean;

    @EJB
    private PostSessionBeanLocal postSessionBean;

    @EJB
    private StudentSessionBeanLocal studentSessionBean;

    @EJB
    private CourseSessionBeanLocal courseSessionBean;

    @PersistenceContext(unitName = "ConnectIS-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(Student.class, 1L) == null) {
            try {
                Student testStudent = new Student("student@nus.edu.sg", "12345", "Student", "One", "password");
                
                Student testStudent2 = new Student("alvin@nus.edu.sg", "123456", "Alvin", "Tor", "password");
                studentSessionBean.createStudent(testStudent);
                studentSessionBean.createStudent(testStudent2);
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
                
                Course is3103 = new Course();
                is3103.setCourseCode("IS3103");
                is3103.setCourseName("Information Systems Leadership and Communication");
                is3103.setDescription("Today’s technology leaders must possess the ability to identify key drivers of business innovation and be able to align technologies with business objectives. This course encompasses the strategic, tactical, and operational aspects of managing and leading IT adoption, implementation, and organizational change to derive strategic value from IT. Students will also learn influential, strategic, and change communication skills, to foster collaboration between technology and business stakeholders. By synthesising critical knowledge areas in information systems management and leadership communication, students will examine the intertwined issues of technology and business for leading digital transformations.");
                is3103.setPosts(new ArrayList<>());
                
                Course is3106 = new Course();
                is3106.setCourseCode("IS3106");
                is3106.setCourseName("Enterprise Systems Interface Design and Development");
                is3106.setDescription("This course aims to train students to be conversant in front-end development for Enterprise Systems. It complements IS2103 which focuses on backend development aspects for Enterprise Systems. Topics covered include: web development scripting languages, web templating design and component design, integrating with backend application, and basic mobile application development.");
                is3106.setPosts(new ArrayList<>());
                
                em.persist(is3103);
                em.persist(is3106);
                
                Course course = courseSessionBean.getCourse((long) 1);
                temp.setCourse(course);
                
                postSessionBean.createPost(temp, ss);
                Post pp = postSessionBean.retrievePostByTitle("Should I SU IS3106 :(");
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
            } catch (NoResultException ex) {
                Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
