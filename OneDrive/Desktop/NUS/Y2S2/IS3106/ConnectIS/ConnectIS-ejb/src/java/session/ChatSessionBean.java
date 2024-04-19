/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Chat;
import entity.Message;
import entity.Student;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author alvintjw
 */
@Stateless
public class ChatSessionBean implements ChatSessionBeanLocal {

    @PersistenceContext(unitName = "ConnectIS-ejbPU")
    private EntityManager em;

    @Override
    public void createChat(Chat newChat) {
        // Assuming newChat.student1 and newChat.student2 contain the IDs of the students.
        Long student1Id = newChat.getStudent1().getId();
        Long student2Id = newChat.getStudent2().getId();

        // Retrieve managed Student entities based on ID.
        Student managedStudent1 = em.find(Student.class, student1Id);
        Student managedStudent2 = em.find(Student.class, student2Id);

        //Only for testing to be delete later
        managedStudent1.getChats().clear();
        managedStudent2.getChats().clear();
        if (managedStudent1 == null || managedStudent2 == null) {
            System.out.println("One of the students does not exist.");
            return; // Or handle appropriately, maybe throw an exception.
        }

        // Set the managed students into newChat.
        newChat.setStudent1(managedStudent1);
        newChat.setStudent2(managedStudent2);

        managedStudent1.getChats().add(newChat);
        managedStudent2.getChats().add(newChat);

        // Add the newChat to both students' chat lists.
        // Print the size of each student's chat list after adding the new chat
        System.out.println("Student1 now has " + managedStudent1.getChats().size() + " chats.");
        System.out.println("Student2 now has " + managedStudent2.getChats().size() + " chats.");

        // Persist the new Chat.
        em.persist(newChat);
    }

    @Override
    public void createMessage(Message newMessage, Long senderId) {
        // Retrieve the Chat associated with the newMessage
        Chat retrievedChat = em.find(Chat.class, newMessage.getChat().getId());

        System.out.println("Before Chat now has " + retrievedChat.getMessages().size() + " messages.");
        // Retrieve the sender Student using the provided senderId
        Student sender = em.find(Student.class, senderId);

        // Initialize a variable for the receiver
        Student receiver;

        // Determine the receiver based on whether the sender is student1 or student2
        if (retrievedChat.getStudent1().equals(sender)) {
            receiver = retrievedChat.getStudent2();
        } else {
            receiver = retrievedChat.getStudent1();
        }

        // Assuming you want to set the sender and receiver on the newMessage
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);

        // Optionally, add the message to the chat messages list if managed within the Chat entity
        retrievedChat.getMessages().add(newMessage);
        System.out.println("After Chat now has " + retrievedChat.getMessages().size() + " messages.");

        // Persist the new message in the EntityManager
        em.persist(newMessage);

        // Depending on your application structure, you might need to commit the transaction or let it be handled automatically
        // em.getTransaction().commit(); (if handling transactions manually)
    }

    @Override
    public Chat findChat(Long chatId) throws NoResultException {
        try {
            return em.find(Chat.class, chatId);
        } catch (NoResultException ex) {
            throw new NoResultException("No chat exist in the database.");
        }

    }
    
    @Override
    public boolean doesChatExistBetweenStudents(Long studentId1, Long studentId2) {
    // Query to check if a chat exists between the two students
    String queryString = "SELECT COUNT(c) FROM Chat c WHERE " +
            "((c.student1.id = :studentId1 AND c.student2.id = :studentId2) OR " +
            "(c.student1.id = :studentId2 AND c.student2.id = :studentId1))";

    // Execute the query
    Long count = em.createQuery(queryString, Long.class)
            .setParameter("studentId1", studentId1)
            .setParameter("studentId2", studentId2)
            .getSingleResult();

    // If count is greater than 0, it means a chat exists between the students
    return count > 0;
}


    @Override
    public List<Chat> findChatsForStudent(Long studentId) {
        TypedQuery<Chat> query = em.createQuery(
                "SELECT c FROM Chat c WHERE c.student1.id = :studentId OR c.student2.id = :studentId", Chat.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }
    
     @Override
    public String getOtherStudentName(Long chatId, Long loggedInStudentId) {
        Chat chat = em.find(Chat.class, chatId);
        if (chat.getStudent1().getId().equals(loggedInStudentId)) {
            return chat.isStudent2Anonymous() ? "Anonymous" : 
                chat.getStudent2().getFirstname() + " " + chat.getStudent2().getLastname();
        } else {
            return chat.isStudent1Anonymous() ? "Anonymous" : 
                chat.getStudent1().getFirstname() + " " + chat.getStudent1().getLastname();
        }
    }

    @Override
    public Student getOtherStudent(Long chatId, Long loggedInStudentId) {
        Chat chat = em.find(Chat.class, chatId);
        if (chat.getStudent1() != null && chat.getStudent1().getId().equals(loggedInStudentId)) {
            return chat.getStudent2();
        } else {
            return chat.getStudent1();
        }
    }

    @Override
    public Message getLastMessage(Long chatId) {
        Chat chat = em.find(Chat.class, chatId);
        List<Message> messages = chat.getMessages();
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1);
        } else {
            return new Message(); // Consider returning null or an Optional<Message> here
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
