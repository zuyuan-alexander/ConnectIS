/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author zuyua
 */
@Local
public interface FlightRouteSessionBeanLocal {
    
    public Long createFlightRoute(FlightRoute flightRoute) throws AirportNotFoundException;
    
    public List<FlightRoute> viewAllFlightRoutes();
    
    public void deleteFlightRoute(Long flightRouteId) throws FlightRouteNotFoundException;
    
    public FlightRoute retrieveFlightRouteByFlightRouteId(Long flightRouteId) throws FlightRouteNotFoundException;
}
