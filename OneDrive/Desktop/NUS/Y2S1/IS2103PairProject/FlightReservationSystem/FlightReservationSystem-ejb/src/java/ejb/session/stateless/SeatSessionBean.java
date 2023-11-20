/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Passenger;
import entity.Seat;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.SeatNotFoundException;

/**
 *
 * @author zuyua
 */
@Stateless
public class SeatSessionBean implements SeatSessionBeanRemote, SeatSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

   
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public SeatSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Seat createSeats(Seat seat) throws InputDataValidationException {
        Set<ConstraintViolation<Seat>>constraintViolations = validator.validate(seat);
        
        if(constraintViolations.isEmpty())
        {

             em.persist(seat);
             return seat;
            
        } else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));

        }
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
    
    @Override
    public List<Seat> retrieveAllReservedSeats(Long cabinClassId) {
        Query query = em.createQuery("SELECT s FROM Passenger p JOIN p.seat s WHERE s.cabinClass.cabinClassId = :inCabinClassId");
        query.setParameter("inCabinClassId", cabinClassId);
        return query.getResultList();
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Seat>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
