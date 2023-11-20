/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import entity.Passenger;
import entity.Seat;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.PassengerAlreadyExistsException;
import util.exception.PassengerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zuyua
 */
@Stateless
public class PassengerSessionBean implements PassengerSessionBeanRemote, PassengerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

      private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public PassengerSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Passenger retrievePassengerByPassportNumber(String passportnum) throws PassengerNotFoundException
    {
         Query query = em.createQuery("SELECT p FROM Passenger p WHERE p.passportNumber = :inPassportnum");
        query.setParameter("inPassportnum", passportnum);
        
        try
        {
            return (Passenger)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new PassengerNotFoundException("Passenger with passport number " + passportnum + " does not exist!");
        }
    }
    
    @Override
    public Long createNewPassenger(Passenger newPassenger, Long seatid, Long fsid) throws PassengerAlreadyExistsException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<Passenger>>constraintViolations = validator.validate(newPassenger);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                Seat chosenseat = em.find(Seat.class, seatid);
                
                FlightSchedule chosenfs = em.find(FlightSchedule.class, fsid);
                newPassenger.setFlightSchedule(chosenfs);
                chosenfs.getPassengers().add(newPassenger);
                
                newPassenger.setSeat(chosenseat);
                
                //chosenseat.setPassenger(newPassenger);
                //chosenseat.
                em.persist(newPassenger);
                em.flush();

                return newPassenger.getPassengerid();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PassengerAlreadyExistsException("You have already reserved this flight");
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Passenger>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
