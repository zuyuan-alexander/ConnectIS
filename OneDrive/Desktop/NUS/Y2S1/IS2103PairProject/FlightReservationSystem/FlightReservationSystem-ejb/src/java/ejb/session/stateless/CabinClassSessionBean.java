/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.Seat;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.SeatStatusEnum;
import util.exception.CabinClassNotFoundException;

/**
 *
 * @author zuyua
 */
@Stateless
public class CabinClassSessionBean implements CabinClassSessionBeanRemote, CabinClassSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private SeatSessionBeanLocal seatSessionBeanLocal;

    public CabinClassSessionBean() {
    }
    
    @Override
    public CabinClass createCabinClass(CabinClass cabinClass) {
        Integer counter = 0;
        
        Integer numOfAisles = cabinClass.getNumOfAisles();
        Integer numOfRows = cabinClass.getNumOfRows();
        Integer numOfSeatsAbreast = cabinClass.getNumOfSeatsAbreast();
        String actualSeatingConfiguration = cabinClass.getActualSeatConfiguration();
        
        Integer maxCapacity = cabinClass.getMaxCapacity();
        
        em.persist(cabinClass);
        em.flush();
        
        // create the seats
        for(int i=1; i<=numOfRows; i++) {
            for (int j=1; j<=numOfSeatsAbreast; j++) {
                if (counter > maxCapacity) {
                    break;
                }
                
                Character seatLetter = (char) ('A' + (j - 1));
                
                Seat seat = new Seat(i, seatLetter, SeatStatusEnum.AVAILABLE);
                
                seatSessionBeanLocal.createSeats(seat);
                
                cabinClass.getSeats().add(seat);
                seat.setCabinClass(cabinClass);
                
                counter++;
            }
        }
        
        return cabinClass;
    }
    
    @Override
    public CabinClass retrievePreferedCabinClassType(List<CabinClass> ccList, CabinClassTypeEnum cabinClassType) {
        for (CabinClass cabinClass : ccList) {
            if (cabinClass.getCabinClassType().equals(cabinClassType)) {
                return cabinClass;
            }
        }
        return null;
    }
    
    @Override
    public CabinClass retrieveCabinClassByID(Long ccid) throws CabinClassNotFoundException
    {
        CabinClass cc = em.find(CabinClass.class, ccid);
        if(cc != null)
        {
          cc.getSeats().size();
          return cc;
        }
        else
        {
            throw new CabinClassNotFoundException("Cabin Class ID " + ccid + " does not exist!");
        }
    }
    
    /*
    @Override
    public Integer calculateNumOfAvailabeSeats(CabinClass cabinClass) {
        List<Seat> seats = cabinClass.getSeats();
        Integer counter = 0;
        
        for (Seat seat : seats) {
            if (seat.getSeatStatus().equals(SeatStatusEnum.AVAILABLE)) {
                counter++;
            }
        }
        
        return counter;
    }
    
    @Override
    public Integer calculateNumOfReservedSeats(CabinClass cabinClass) {
        List<Seat> seats = cabinClass.getSeats();
        Integer counter = 0;
        
        for (Seat seat : seats) {
            if (seat.getSeatStatus().equals(SeatStatusEnum.RESERVED)) {
                counter++;
            }
        }
        
        return counter;
    }
    
    @Override
    public List<Integer> calculateNumOfSeats(CabinClass cabinClass) {
        List<Integer> answer = new ArrayList<>();
        Integer numOfAvailableSeats = calculateNumOfAvailabeSeats(cabinClass);
        answer.add(numOfAvailableSeats);
        
        Integer numOfReservedSeats = calculateNumOfReservedSeats(cabinClass);
        answer.add(numOfReservedSeats);
        
        Integer numOfBlanceSeats = cabinClass.getMaxCapacity() - numOfAvailableSeats - numOfReservedSeats;
        answer.add(numOfAvailableSeats);
        
        return answer;
    }
    */
}