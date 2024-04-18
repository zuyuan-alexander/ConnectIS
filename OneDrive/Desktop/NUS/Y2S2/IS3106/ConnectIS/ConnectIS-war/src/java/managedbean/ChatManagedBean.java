/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Chat;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import session.ChatSessionBeanLocal;

/**
 *
 * @author alvintjw
 */
@Named(value = "chatManagedBean")
@ViewScoped
public class ChatManagedBean implements Serializable {

    @EJB
    private ChatSessionBeanLocal chatSessionBean;

    @Inject
    private AuthenticationManagedBean authenBean;
    
    @Inject
    private OtherManagedBean otherBean;

    private Student loggedinStudent;
    private Chat selectedChat;
    private Long selectedChatId;
    private Student student1;
    private Student student2;
    private boolean student1Anonymous;
    private boolean student2Anonymous;

    /**
     * Creates a new instance of ChatManagedBean
     */
    public ChatManagedBean() {
    }

    @PostConstruct
    public void Init() {
        if (selectedChatId != null) {
            loadSelectedChat();
        }
    }

    public void loadSelectedChat() {
        selectedChat = chatSessionBean.findChat(selectedChatId);
        if (selectedChat.getStudent1().equals(loggedinStudent)) {
            student1 = selectedChat.getStudent1();
            student1Anonymous = selectedChat.isStudent1Anonymous();
            student2 = selectedChat.getStudent2();
            student2Anonymous = selectedChat.isStudent2Anonymous();
        } else {
            student2 = selectedChat.getStudent1();
            student2Anonymous = selectedChat.isStudent1Anonymous();
            student1 = selectedChat.getStudent2();
            student1Anonymous = selectedChat.isStudent2Anonymous();
        }

    }
    
    public void createNormalChat() {
        Student otherStudent = otherBean.getOtherStudent();
        Chat newChat = new Chat(loggedinStudent, otherStudent, false, false);
        chatSessionBean.createChat(newChat);

    }

    public Student getStudent1() {
        return student1;
    }

    public void setStudent1(Student student1) {
        this.student1 = student1;
    }

    public Student getStudent2() {
        return student2;
    }

    public void setStudent2(Student student2) {
        this.student2 = student2;
    }

    public Long getSelectedChatId() {
        return selectedChatId;
    }

    public void setSelectedChatId(Long selectedChatId) {
        this.selectedChatId = selectedChatId;
    }

    public boolean isStudent1Anonymous() {
        return student1Anonymous;
    }

    public void setStudent1Anonymous(boolean student1Anonymous) {
        this.student1Anonymous = student1Anonymous;
    }

    public boolean isStudent2Anonymous() {
        return student2Anonymous;
    }

    public void setStudent2Anonymous(boolean student2Anonymous) {
        this.student2Anonymous = student2Anonymous;
    }

 
    

    public Student getLoggedinStudent() {
        return loggedinStudent;
    }

    public void setLoggedinStudent(Student loggedinStudent) {
        this.loggedinStudent = loggedinStudent;
    }

    public Chat getSelectedChat() {
        return selectedChat;
    }

    public void setSelectedChat(Chat selectedChat) {
        this.selectedChat = selectedChat;
    }

}
