/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author alvintjw
 */
@Entity
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Student student1;
    private Student student2;
    private boolean student1Anonymous;
    private boolean student2Anonymous;
    private Message lastMessage;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    public Chat(Student student1, Student student2, boolean isStudent1Anonymous, boolean isStudent2Anonymous) {
        this.student1 = student1;
        this.student2 = student2;
        this.student1Anonymous = isStudent1Anonymous;
        this.student2Anonymous = isStudent2Anonymous;
    }

    public Chat() {
    }

    public String getOtherStudentName(Long loggedInStudentId) {
        if (student1.getId().equals(loggedInStudentId)) {
            return student2Anonymous ? "Anonymous" : student2.getFirstname() +  " " + student2.getLastname();
        } else {
            return student1Anonymous ? "Anonymous" : student1.getFirstname() +  " " + student2.getLastname();
        }
    }

    public Message getLastMessage() {
        lastMessage = messages.get(messages.size());
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chat)) {
            return false;
        }
        Chat other = (Chat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Chat[ id=" + id + " ]";
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

 

   
 

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
