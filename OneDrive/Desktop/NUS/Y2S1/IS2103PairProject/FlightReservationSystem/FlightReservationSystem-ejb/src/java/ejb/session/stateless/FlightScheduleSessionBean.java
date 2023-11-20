/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.enumeration.CabinClassTypeEnum;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author alvintjw
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private SeatSessionBeanLocal seatSessionBeanLocal;

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public Long createNewFlightSchedule(FlightSchedule fs, Long newFSPid)
    {
        FlightSchedulePlan newFSP = em.find(FlightSchedulePlan.class, newFSPid);
        fs.setFlightSchedulePlan(newFSP);
        newFSP.getFlightschedules().add(fs);
        em.persist(fs);
        em.flush();
        return fs.getFlightscheduleid();
    }
    
  
    
    @Override
    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleid) throws FlightScheduleNotFoundException
    {
        FlightSchedule f = em.find(FlightSchedule.class, flightScheduleid);
        
        if(f != null)
        {
            f.getFlightSchedulePlan().getFares().size();
            f.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses().size();
            f.getPassengers().size();
            return f;
        }
        else
        {
            throw new FlightScheduleNotFoundException("Staff ID " + flightScheduleid + " does not exist!");
        }
    }
    
    @Override
    public List<FlightSchedule> retrieveFlightScheduleByDate(Date date, String flightNumber) {
        /*
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDate = :inDate ORDER BY fs.flightscheduleid ASC");
        query.setParameter("inDate", date);
        return query.getResultList();
        */
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDate = :inDate AND fs.flightSchedulePlan.flight.flightNumber = :inFLightNumber ORDER BY fs.flightscheduleid ASC");
        query.setParameter("inDate", date).setParameter("inFLightNumber", flightNumber);
        return query.getResultList();
    }
    
    @Override
    public List<FlightSchedule> checkFlightScheduleWithPreferedCabinClass(List<FlightSchedule> fsList, CabinClassTypeEnum cabinClassType) {
        List<FlightSchedule> answer = new ArrayList<>();
        
        for (FlightSchedule fs : fsList) {
            for (CabinClass cabinClass : fs.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses()) {
                // if the list of cabin class for the flight schedule contains the prefered cabin class, add into the answer list
                if (cabinClass.getCabinClassType().equals(cabinClassType)) {
                    answer.add(fs);
                }
            }
        }
        return answer;
    }
    
    @Override
    public List<FlightSchedule> retrieveAllFlightSchedulesWithFSPid(Long fspid)
    {
         TypedQuery<FlightSchedule> query = em.createQuery(
          "SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flightscheduleplanid = :flightSchedulePlanId",
        FlightSchedule.class
    );
    query.setParameter("flightSchedulePlanId", fspid);
    return query.getResultList();
    }
    

    /*
    public void seatInit(FlightSchedule flightSchedule, CabinClass cabinClass) {
        Integer counter = 0;
        
        Integer numOfAisles = cabinClass.getNumOfAisles();
        Integer numOfRows = cabinClass.getNumOfRows();
        Integer numOfSeatsAbreast = cabinClass.getNumOfSeatsAbreast();
        String actualSeatingConfiguration = cabinClass.getActualSeatConfiguration();
        
        Integer maxCapacity = cabinClass.getMaxCapacity();
        
        // create the seats
        for(int i=1; i<=numOfRows; i++) {
            for (int j=1; j<=numOfSeatsAbreast; j++) {
                if (counter > maxCapacity) {
                    break;
                }
                
                char seatLetter = (char) ('A' + (j - 1));
                
                Seat seat = new Seat(i, seatLetter, SeatStatusEnum.AVAILABLE, cabinClass.getCabinClassType());
                seat.setFlightSchedule(flightSchedule);
                flightSchedule.getSeats().add(seat);
                
                seatSessionBeanLocal.createSeats(seat);
                
                //cabinClass.getSeats().add(seat);
                //seat.setCabinClass(cabinClass);
                // seat.setFlightSchedule(flighgSchedule);
                
                counter++;
            }
        }
    }
*/
}
