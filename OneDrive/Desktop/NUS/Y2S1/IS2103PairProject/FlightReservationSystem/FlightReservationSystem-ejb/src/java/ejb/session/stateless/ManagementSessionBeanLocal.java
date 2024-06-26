/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;
import entity.Passenger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zuyua
 */
@Local
public interface ManagementSessionBeanLocal {
    
    public List<Passenger> viewSeatsInventory(CabinClass cabinClass, FlightSchedule flightSchedule);
    
}
