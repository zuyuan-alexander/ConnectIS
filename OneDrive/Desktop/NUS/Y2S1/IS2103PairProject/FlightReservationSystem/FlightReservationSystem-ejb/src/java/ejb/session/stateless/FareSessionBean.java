/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.Fare;
import entity.FlightSchedulePlan;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author zuyua
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBeanLocal;

    public FareSessionBean() {
    }
    
    @Override
    public Long createNewFare(Fare fare,  Long newFSPid) {
        //FlightSchedulePlan fsp = flightSchedulePlanSessionBeanLocal.retrieveStaffByStaffId(fspId);
        FlightSchedulePlan newFSP = em.find(FlightSchedulePlan.class, newFSPid);
    
        fare.setFlightSchedulePlan(newFSP);
        newFSP.getFares().size();
        newFSP.getFares().add(fare);
        
        em.persist(fare);
        em.flush();
        
        return fare.getFareid();
    }
    
    @Override
    public Fare retrieveFareAmountByCabinClassType(List<Fare> fares, CabinClass cabinClass) {
        for (Fare fare : fares) {
            if (fare.getCabinClassType().equals(cabinClass.getCabinClassType())) {
                return fare;
            }
        }
        return null;
    }
    
}
