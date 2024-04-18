/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Chat;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

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
        em.persist(newChat);
    }
    
    @Override
    public Chat findChat(Long chatId) throws NoResultException{
        try {
            return em.find(Chat.class, chatId);
        } catch (NoResultException ex) {
             throw new NoResultException("No chat exist in the database.");
        }
        
    }
 

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
