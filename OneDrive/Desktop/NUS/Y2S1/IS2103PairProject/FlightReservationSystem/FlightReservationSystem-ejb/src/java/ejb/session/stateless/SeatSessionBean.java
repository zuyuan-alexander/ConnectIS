/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Seat;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.SeatNotFoundException;

/**
 *
 * @author zuyua
 */
@Stateless
public class SeatSessionBean implements SeatSessionBeanRemote, SeatSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    public SeatSessionBean() {
    }

    @Override
    public Seat createSeats(Seat seat) {
        em.persist(seat);
        return seat;
    }
    
    @Override
    public Seat retrieveSeatBySeatLetterAndRowNumber(Character seatLetter, Integer rowNumber, Long cabinClassId) throws SeatNotFoundException { 
        try
        {
             Query query = em.createQuery("SELECT s FROM Seat s WHERE s.seatLetter = :inSeatLetter AND s.rowNumber = :inRowNumber AND s.cabinClass.cabinClassId = :inCabinClassId");
             query.setParameter("inSeatLetter", seatLetter).setParameter("inRowNumber", rowNumber).setParameter("inCabinClassId", cabinClassId);
             return (Seat) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex)
        {
            throw new SeatNotFoundException("Seat " + rowNumber + ""+ (char)seatLetter +" does not exist");
        }
       
    }
    
    @Override
    public Seat retrieveSeatBySeatId(Long id) throws SeatNotFoundException {
        Seat seat = em.find(Seat.class, id);
        
        if(seat != null) {
            return seat;
        } else {
            throw new SeatNotFoundException("Seat with Seat Id " + id + " does not exist!");
        }
    }
    

    
}
