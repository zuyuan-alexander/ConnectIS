/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Passenger;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface FlightReservationSessionBeanRemote {
    
    public List<FlightSchedule> searchFlightDirectFlight(String departureAirport, String destinationAirport, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType);

    public List<Object[]> searchFlightConnectingFlight(String departureAirport, String destinationAirport, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType);

    public List<FlightReservation> viewMyFlightReservations(Long customerId);

    public FlightReservation retrieveFlightReservationById(Long flightReservationId) throws FlightReservationNotFoundException;

    public Long reserveFlightMain(Long customerid, Long fsid, Long passengerid, TripTypeEnum tripType);
    
    public Long createNewFlightReservation(Long customerId, Long flightScheduleId, Passenger passenger, Long seatId, TripTypeEnum tripType) throws CustomerNotFoundException, FlightScheduleNotFoundException, SeatNotFoundException;
    
    //public List<FlightSchedule> searchFlightConnectingFlightSecond(String departureAirport, String destinationAirport, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType);
    
}
