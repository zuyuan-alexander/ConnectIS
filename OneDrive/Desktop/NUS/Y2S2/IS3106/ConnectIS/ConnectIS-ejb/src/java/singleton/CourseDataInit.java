package singleton;

import entity.Course;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class CourseDataInit {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        if (em.find(Course.class, 1L) == null) {
            createEventData();
        }
    }

    private void createEventData() {

        try {
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

        } catch (Exception ex) {
            Logger.getLogger(CourseDataInit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
