/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Chat;
import entity.Message;
import entity.Student;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
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
    private List<Chat> chats;
    private Chat selectedChat;
    private Long selectedChatId;
    private String content;
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

        loggedinStudent = authenBean.getLoggedinStudent();
        chats = chatSessionBean.findChatsForStudent(loggedinStudent.getId());
        if (loggedinStudent.getChats().isEmpty()) {
            System.out.println("In Init, size of loggedinStudentChats: 0");
            System.out.println("In Init, size of loggedinStudentChats: 0");
        } else {
            System.out.println("In Init, size of loggedinStudentChats: " + loggedinStudent.getChats().size());
        }

        if (selectedChatId != null) {
            loadSelectedChat(selectedChatId);
        }
    }

    public void loadSelectedChat(Long chatId) {
        System.out.println("loadSelected chat is called with chatId: " + chatId);

        selectedChat = chatSessionBean.findChat(chatId);
        if (selectedChat.getMessages().isEmpty()) {
            System.out.println("SelectedChat now has " + 0 + " messages.");
        } else {
            System.out.println("SelectedChat now has " + selectedChat.getMessages().size() + " messages.");
        }

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

    public String createNormalChat() {
        Student otherStudent = otherBean.getOtherStudent();
        Boolean chatExist = chatSessionBean.doesChatExistBetweenStudents(otherStudent.getId(), loggedinStudent.getId());
        System.out.println("loggedinStudent name: " + loggedinStudent.getFirstname());
        System.out.println("otherStudent name: " + otherStudent.getFirstname());
        if (loggedinStudent != null & otherStudent != null & !loggedinStudent.equals(otherStudent) & !chatExist) {
            Chat newChat = new Chat(loggedinStudent, otherStudent, false, false);
            chatSessionBean.createChat(newChat);
            return "viewChats.xhtml?faces-redirect=true";
        }

        return null;
    }

    public void sendMessage() {
        System.out.println("Send message is being called");
        Message m = new Message();
        m.setContent(content);
        if (selectedChat != null) {
            m.setChat(selectedChat);
            if (loggedinStudent != null) {
                chatSessionBean.createMessage(m, loggedinStudent.getId());
            }
        }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

}
