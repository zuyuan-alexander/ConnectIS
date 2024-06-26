/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Chat;
import entity.Message;
import entity.Student;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author alvintjw
 */
@Local
public interface ChatSessionBeanLocal {

    public Chat findChat(Long chatId) throws NoResultException;

    public void createChat(Chat newChat);

    public void createMessage(Message newMessage, Long senderId);

    public List<Chat> findChatsForStudent(Long studentId);
    
     public boolean doesChatExistBetweenStudents(Long studentId1, Long studentId2);

    public String getOtherStudentName(Long chatId, Long loggedInStudentId);

    public Student getOtherStudent(Long chatId, Long loggedInStudentId);

    public Message getLastMessage(Long chatId);

}
