/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Passenger;
import entity.Seat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.TripTypeEnum;
import util.exception.CustomerNotFoundException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.SeatNotFoundException;

/**
 *
 * @author zuyua
 */
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;
    
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @EJB
    private SeatSessionBeanLocal seatSessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public FlightReservationSessionBean()
    {
        
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
   

    @Override
    public List<FlightSchedule> searchFlightDirectFlight(String departureAirport, String destinationAirport, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        // String jpql = "SELECT fs FROM FlightSchedule fs WHERE fs.flight.departureAirport = :departureAirport " + "AND fs.flight.destinationAirport = :destinationAirport AND fs.departureDate = :departureDate";
        /*
        List<FlightSchedule> flightSchedulesOnDate = flightScheduleSessionBeanLocal.retrieveFlightScheduleForReservation(departureAirport, destinationAirport, date);
        List<FlightSchedule> flightSchedulesThreeDaysBeforeDate;
        List<FlightSchedule> flightScheduleThreeDaysAfterDate;
        */
        // display the flight schedule availability and cabin class availability in the client
        // display the price per passenger and total price for all passenger in the client (retrieve fare from the flight schedule)
        
        /*
        if (cabinClass == null) {
            Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDate = :inDepartureDate AND fs.flightSchedulePlan.flight.flightRoute.origin = :inOrigin AND fs.flightSchedulePlan.flight.flightRoute.destination = :inDestination");
            query.setParameter("inDepartureDate", date).setParameter("inOrigin", departureAirport).setParameter("inDestination", departureAirport);
            return query.getResultList();
        } else {
            Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDate = :inDepartureDate AND fs.flightSchedulePlan.flight.flightRoute.origin = :inOrigin AND fs.flightSchedulePlan.flight.flightRoute.destination = :inDestination");
            query.setParameter("inDepartureDate", date).setParameter("inOrigin", departureAirport).setParameter("inDestination", departureAirport);
            return query.getResultList();
        } */
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDate = :inDepartureDate AND fs.flightSchedulePlan.flight.flightRoute.origin = :inOrigin AND fs.flightSchedulePlan.flight.flightRoute.destination = :inDestination");
        query.setParameter("inDepartureDate", date).setParameter("inOrigin", departureAirport).setParameter("inDestination", destinationAirport);
        List<FlightSchedule> fsList = query.getResultList();
        
        if (cabinClassType == null) {
            // no prefered cabin class
            return fsList;
        } else {
            // has prefered cabin class
            return flightScheduleSessionBeanLocal.checkFlightScheduleWithPreferedCabinClass(fsList, cabinClassType);
        }
    }
    
    @Override
    public List<Object[]> searchFlightConnectingFlight(String departureAirport, String destinationAirport, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        Query query = em.createQuery("SELECT DISTINCT fs1, fs2 FROM FlightSchedule fs1" + 
                ", FlightSchedule fs2 WHERE fs1.flightSchedulePlan.flight.flightRoute.destination = fs2.flightSchedulePlan.flight.flightRoute.origin " + 
                "AND fs1.flightSchedulePlan.flight.flightRoute.origin = :inOrigin " +
                "AND fs2.flightSchedulePlan.flight.flightRoute.destination = :inDestination " +
                "AND fs1.departureDate = :inDepartureDate " + 
                "AND fs1.arrivalDate <= fs2.departureDate " + 
                "AND fs1.arrivalTime < fs2.departureTime");
        query.setParameter("inDepartureDate", date).setParameter("inOrigin", departureAirport).setParameter("inDestination", destinationAirport);
        return query.getResultList();
    }
    
    /*
    @Override
    public List<FlightSchedule> searchFlightConnectingFlightSecond(String departureAirport, String destinationAirport, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        Query query = em.createQuery("SELECT DISTINCT fs2 FROM FlightSchedule fs2" + 
                ", FlightSchedule fs1 WHERE fs1.flightSchedulePlan.flight.flightRoute.destination = fs2.flightSchedulePlan.flight.flightRoute.origin " + 
                "AND fs1.flightSchedulePlan.flight.flightRoute.origin = :inOrigin " +
                "AND fs2.flightSchedulePlan.flight.flightRoute.destination = :inDestination " +
                "AND fs1.departureDate = :inDepartureDate");
        query.setParameter("inDepartureDate", date).setParameter("inOrigin", departureAirport).setParameter("inDestination", destinationAirport);
        return query.getResultList();
    }
  */
    
    // reserve flight will create a passenger from customer
    
    @Override
    public List<FlightReservation> viewMyFlightReservations(Long customerId) {
        Query query = em.createQuery("SELECT fr FROM FlightReservation fr WHERE fr.customer.customerid = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }
    
    // viewMyFlightReservationDetails in the client (by retrieving the flight reservation using the id)
    
    @Override
    public FlightReservation retrieveFlightReservationById(Long flightReservationId) throws FlightReservationNotFoundException {
        FlightReservation flightReservation = em.find(FlightReservation.class, flightReservationId);
        
        if(flightReservation != null)
        {
            return flightReservation;
        }
        else
        {
            throw new FlightReservationNotFoundException("Flight Reservation with Id " + flightReservationId + " does not exist!");
        }
    }
    
    @Override
    public Long reserveFlightMain(Long customerid, Long fsid, Long passengerid, TripTypeEnum tripType) {
        FlightReservation flightReservation = new FlightReservation(tripType);
        Customer customer = em.find(Customer.class, customerid);
        FlightSchedule flightSchedule = em.find(FlightSchedule.class, fsid);
        Passenger passenger = em.find(Passenger.class, passengerid);
        
        customer.getFlightReservations().add(flightReservation);
        flightReservation.setCustomer(customer);
        
        passenger.setFlightreservation(flightReservation);
        flightReservation.setPassenger(passenger);
        
        flightReservation.setFlightSchedules(flightSchedule);
        flightSchedule.getFlightReservation().add(flightReservation);
        
        flightSchedule.getPassengers().add(passenger);
        passenger.setFlightSchedule(flightSchedule);
        
        em.persist(flightReservation);
        em.flush();
        
        return flightReservation.getFlightreservationid();
    }
    
    @Override
    public Long createNewFlightReservation(Long customerId, Long flightScheduleId, Passenger passenger, Long seatId, TripTypeEnum tripType) throws CustomerNotFoundException, FlightScheduleNotFoundException, SeatNotFoundException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        FlightSchedule flightSchedule = flightScheduleSessionBeanLocal.retrieveFlightScheduleById(flightScheduleId);
        Seat seat = seatSessionBeanLocal.retrieveSeatBySeatId(seatId);
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, flightSchedule.getFlightSchedulePlan().getFlightscheduleplanid());
        FlightReservation flightReservation = new FlightReservation(tripType);
        
        flightReservation.setCustomer(customer);
        customer.getFlightReservations().add(flightReservation);
        
        flightReservation.setFlightSchedules(flightSchedule);
        flightSchedule.getFlightReservation().add(flightReservation);
        
        flightReservation.setSeat(seat);
        
        passenger.setFlightSchedule(flightSchedule);
        passenger.setFlightreservation(flightReservation);
        passenger.setSeat(seat);
        flightSchedule.getPassengers().add(passenger);
        flightReservation.setPassenger(passenger);
        
        fsp.setReserved(Boolean.TRUE);
        
        em.persist(fsp);
        em.persist(passenger);
        em.persist(flightReservation);
        em.flush();
        
        return flightReservation.getFlightreservationid();
    }
    
    
     private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightReservation>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    
}
