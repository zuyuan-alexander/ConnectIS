/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftSessionBeanLocal;
import ejb.session.stateless.FareSessionBeanLocal;
import ejb.session.stateless.FlightRouteSessionBeanLocal;
import ejb.session.stateless.FlightSchedulePlanSessionBeanLocal;
import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.FlightSessionBeanLocal;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.Airport;
import entity.CabinClass;
import entity.Customer;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Partner;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.EmployeeTypeEnum;
import util.enumeration.ScheduleTypeEnum;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightDisabledException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistsException;
import util.exception.FlightRouteAlreadyExistedException;
import util.exception.FlightRouteDisabledException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OverlappingScheduleException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;

/**
 *
 * @author zuyua
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBeanLocal;
    
    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBeanLocal;
    
    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;
    
    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;
    
    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBeanLocal;
    
    @EJB
    private FareSessionBeanLocal fareSessionBeanLocal;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        initEmployee();
        initPartner();
        initAirport();
        initAircraftType();
        initAircraftConfiguration();
        initFlightRoute();
        initFlight();
        //initFlightSchedulePlan();
    }
    
    public void initEmployee() {
        Employee employee = new Employee("Fleet Manager", "fleetmanager", "password", EmployeeTypeEnum.FLEET_MANAGER);
        em.persist(employee);
        em.flush();
        
        employee = new Employee("Route Planner", "routeplanner", "password", EmployeeTypeEnum.ROUTE_MANAGER);
        em.persist(employee);
        em.flush();
        
        employee = new Employee("Schedule Manager", "schedulemanager", "password", EmployeeTypeEnum.SCHEDULE_MANAGER);
        em.persist(employee);
        em.flush();
        
        employee = new Employee("Sales Manager", "salesmanager", "password", EmployeeTypeEnum.SALES_MANAGER);
        em.persist(employee);
        em.flush();
    }
    
    public void initPartner() {
        Partner partner = new Partner("Holiday.com", "holidaydotcom", "password");
        em.persist(partner);
        em.flush();
    }
    
    public void initAirport() {
        Airport airport = new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore");
        em.persist(airport);
        em.flush();
        
        airport = new Airport("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China");
        em.persist(airport);
        em.flush();
        
        airport = new Airport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C");
        em.persist(airport);
        em.flush();
        
        airport = new Airport("Narita", "NRT", "Narita", "Chiba", "Japan");
        em.persist(airport);
        em.flush();
        
        airport = new Airport("Sydney", "SYD", "Sydney", "New South Wales", "Australia");
        em.persist(airport);
        em.flush();
    }
    
    public void initAircraftType() {
        AircraftType aircraft = new AircraftType("Boeing 737", 200);
        em.persist(aircraft);
        em.flush();
        
        aircraft = new AircraftType("Boeing 747", 400);
        em.persist(aircraft);
        em.flush();
    }
    
    public void initAircraftConfiguration() {
        // Boeing 737 All Economy
        List<CabinClass> cabinClassList = new ArrayList<>();
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration("Boeing 737 All Economy", 1, 180);
        CabinClass cabinClass = new CabinClass(CabinClassTypeEnum.Y, 1, 30, 6, "3-3", 180);
        cabinClassList.add(cabinClass);
        try {
            AircraftType aircraft = aircraftSessionBeanLocal.retrieveAircraftByAircraftTypeName("Boeing 737");
            aircraftSessionBeanLocal.createAircraftConfiguration(aircraftConfiguration, aircraft, cabinClassList);
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        // Boeing 737 Three Classes
        cabinClassList = new ArrayList<>();
        aircraftConfiguration = new AircraftConfiguration("Boeing 737 Three Classes", 3, 180);
        CabinClass class1 = new CabinClass(CabinClassTypeEnum.F, 1, 5, 2, "1-1", 10);
        CabinClass class2 = new CabinClass(CabinClassTypeEnum.J, 1, 5, 4, "2-2", 20);
        CabinClass class3 = new CabinClass(CabinClassTypeEnum.Y, 1, 25, 6, "3-3", 150);
        cabinClassList.add(class1);
        cabinClassList.add(class2);
        cabinClassList.add(class3);
        try {
            AircraftType aircraft = aircraftSessionBeanLocal.retrieveAircraftByAircraftTypeName("Boeing 737");
            aircraftSessionBeanLocal.createAircraftConfiguration(aircraftConfiguration, aircraft, cabinClassList);
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        // Boeing 747 All Economy
        cabinClassList = new ArrayList<>();
        aircraftConfiguration = new AircraftConfiguration("Boeing 747 All Economy", 1, 380);
        cabinClass = new CabinClass(CabinClassTypeEnum.Y, 2, 38, 10, "3-4-3", 380);
        cabinClassList.add(cabinClass);
        try {
            AircraftType aircraft = aircraftSessionBeanLocal.retrieveAircraftByAircraftTypeName("Boeing 747");
            aircraftSessionBeanLocal.createAircraftConfiguration(aircraftConfiguration, aircraft, cabinClassList);
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        // Boeing 747 Three Classes
        cabinClassList = new ArrayList<>();
        aircraftConfiguration = new AircraftConfiguration("Boeing 747 Three Classes", 3, 360);
        class1 = new CabinClass(CabinClassTypeEnum.F, 1, 5, 2, "1-1", 10);
        class2 = new CabinClass(CabinClassTypeEnum.J, 2, 5, 6, "2-2-2", 30);
        class3 = new CabinClass(CabinClassTypeEnum.Y, 2, 32, 10, "3-4-3", 320);
        cabinClassList.add(class1);
        cabinClassList.add(class2);
        cabinClassList.add(class3);
        try {
            AircraftType aircraft = aircraftSessionBeanLocal.retrieveAircraftByAircraftTypeName("Boeing 747");
            aircraftSessionBeanLocal.createAircraftConfiguration(aircraftConfiguration, aircraft, cabinClassList);
        } catch (AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
    }
    
    public void initFlightRoute() {
        // SIN-HKG
        FlightRoute flightRoute = new FlightRoute("SIN", "HKG");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        //SIN-TPE
        flightRoute = new FlightRoute("SIN", "TPE");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        //SIN-NRT
        flightRoute = new FlightRoute("SIN", "NRT");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        //HKG-NRT
        flightRoute = new FlightRoute("HKG", "NRT");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        // TPE-NRT
        flightRoute = new FlightRoute("TPE", "NRT");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        // SIN-SYD
        flightRoute = new FlightRoute("SIN", "SYD");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        // SYD-NRT
        flightRoute = new FlightRoute("SYD", "NRT");
        flightRoute.setReturnFlight(Boolean.TRUE);
        try {
            flightRouteSessionBeanLocal.createFlightRoute(flightRoute);
        } catch (AirportNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
    }
    
    public void initFlight() {
        try {
            Flight flight = new Flight("ML111");
            FlightRoute flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("SIN", "HKG");
            AircraftConfiguration acn = aircraftSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 Three Classes");
            Long flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML112", "Boeing 737 Three Classes");
            
            flight = new Flight("ML211");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("SIN", "TPE");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML212", "Boeing 737 Three Classes");
            
            flight = new Flight("ML311");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("SIN", "NRT");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML312", "Boeing 737 Three Classes");
            
            flight = new Flight("ML411");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("HKG", "NRT");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML412", "Boeing 737 Three Classes");
            
            flight = new Flight("ML511");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("TPE", "NRT");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML512", "Boeing 737 Three Classes");
            
            flight = new Flight("ML611");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("SIN", "SYD");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML612", "Boeing 737 Three Classes");
            
            flight = new Flight("ML621");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("SIN", "SYD");
            acn = aircraftSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 737 All Economy");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML622", "Boeing 737 All Economy");
            
            flight = new Flight("ML711");
            flightRoute = flightRouteSessionBeanLocal.retrieveFlightRouteByOriginDestination("SYD", "NRT");
            acn = aircraftSessionBeanLocal.retrieveAircraftConfigurationByName("Boeing 747 Three Classes");
            flightId = flightSessionBeanLocal.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
            flightSessionBeanLocal.createComplementaryFlight(flightId , "ML712", "Boeing 747 Three Classes");
            
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightNumberExistsException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UpdateFlightException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteDisabledException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void initFlightSchedulePlan() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBeanLocal.retrieveFlightByFlightNumber("ML711");
        } catch (FlightNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        Flight compf = new Flight();
        boolean hasComFlight = (f.getComplimentaryFlight() != null);
        
        if(hasComFlight)
        {
            try
            {
               compf = flightSessionBeanLocal.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBeanLocal.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBeanLocal.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");

        newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
        System.out.println("Enter Day Of Week> ");
        String dayOfWeek = "Monday";
        System.out.println("Enter Departure Time> eg: 9:00 AM");
        String departureTimestr = "9:00 AM";
        System.out.println("Enter Start Date> dd MMM yy");
        String startDateStr = "1 Dec 23";
        System.out.println("Enter End Date> dd MMM yy");
        String endDateStr = "31 Dec 23";
        System.out.println("Enter Flight Duration> HH Hours mm Minutes");
        String flightDurationStr = "14 Hours 00 Minutes";
        String layoverDurationStr = "";
        if(hasComFlight)
        {
            System.out.println("Enter Layover Duration for " + compf.getFlightNumber() + "> HH Hours mm Minutes");
            layoverDurationStr = "2 Hours 00 Minutes";
        }

        //set the departureTime and flightDuration of the new FS

        //call the FS sessionBean to create the FS


        // Parse dates and FlightDuration
        try
        {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            Date flightDuration = timeFormat.parse(flightDurationStr);
            Date departureTime = departureFormat.parse(departureTimestr);
            Date layoverDuration = new Date();

            newFSP.setDayOfWeek(dayOfWeek);
            newFSP.setStartDate(startDate);
            newFSP.setEndDate(endDate);
            newFSP.setNdays(7);
            newFS.setDepartureDate(startDate);
            newFS.setDepartureTime(departureTime);
            newFS.setEstimatedFlightDuration(flightDuration);
            newFS.calculateAndSetArrivalDateTime();

            for (FlightSchedule fs : ongoingfs) 
            {
                if (checkOverlap(newFS, fs)) 
                {       
                    throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");
                }
            }

            Long newfspid = flightSchedulePlanSessionBeanLocal.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBeanLocal.createNewFlightSchedule(newFS, newfspid);
            Long compfspid = Long.MAX_VALUE; 
            Long compfsid = Long.MAX_VALUE;

            if(hasComFlight)
            {
                layoverDuration = timeFormat.parse(layoverDurationStr);
                FlightSchedule temp = new FlightSchedule();
                temp.setEstimatedFlightDuration(layoverDuration);
                temp.setDepartureDate(newFS.getArrivalDate());
                temp.setDepartureTime(newFS.getArrivalTime());
                temp.calculateAndSetArrivalDateTime();

                compFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
                compFSP.setDayOfWeek(dayOfWeek);
                compFSP.setStartDate(startDate);
                compFSP.setEndDate(endDate);
                compFSP.setNdays(7);
                compFS.setDepartureDate(temp.getArrivalDate());
                compFS.setDepartureTime(temp.getArrivalTime());
                compFS.setEstimatedFlightDuration(flightDuration);
                compFS.calculateAndSetArrivalDateTime();


                 compfspid = flightSchedulePlanSessionBeanLocal.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                 compfsid = flightScheduleSessionBeanLocal.createNewFlightSchedule(compFS, compfspid);            
            }

            while(newFS.getDepartureDate().before(endDate))
            {
                for (FlightSchedule fs : ongoingfs) 
                {
                    if (checkOverlap(newFS, fs)) 
                    {

                        throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");
                    }
                }

                //add the main fs
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newFS.getDepartureDate());
                calendar.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                newFS.setDepartureDate(calendar.getTime());
                newFS.calculateAndSetArrivalDateTime(); 
                newfsid = flightScheduleSessionBeanLocal.createNewFlightSchedule(newFS, newfspid);


                //add the returning fs
                if(hasComFlight)
                {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(newFS.getDepartureDate());
                    calendar2.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                    compFS.setDepartureDate(calendar.getTime());
                    compFS.calculateAndSetArrivalDateTime(); 
                    compfsid = flightScheduleSessionBeanLocal.createNewFlightSchedule(compFS, compfspid);

                }

            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(6000), CabinClassTypeEnum.F);
            fareSessionBeanLocal.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(3000), CabinClassTypeEnum.J);
            fareSessionBeanLocal.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(1000), CabinClassTypeEnum.Y);
            fareSessionBeanLocal.createNewFare(fare, newfspid);

        } catch (OverlappingScheduleException ex) 
        {
            System.out.println(ex.getMessage());
        } catch (ParseException ex)
        {
            ex.printStackTrace();
        } catch (InputDataValidationException ex)
        {
            System.out.println(ex.getMessage());
        } 

        catch (FlightDisabledException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public boolean checkOverlap(FlightSchedule schedule1, FlightSchedule schedule2) {
        // Combining departure date and time for schedule1
        Date departureDateTime1 = combineDateTime(schedule1.getDepartureDate(), schedule1.getDepartureTime());
        Date arrivalDateTime1 = combineDateTime(schedule1.getDepartureDate(), schedule1.getArrivalTime());

        // Combining departure date and time for schedule2
        Date departureDateTime2 = combineDateTime(schedule2.getDepartureDate(), schedule2.getDepartureTime());
        Date arrivalDateTime2 = combineDateTime(schedule2.getDepartureDate(), schedule2.getArrivalTime());

        // Checking for overlap
        boolean isOverlap =
                (departureDateTime1.before(arrivalDateTime2) && departureDateTime1.after(departureDateTime2)) ||
                        (arrivalDateTime1.after(departureDateTime2) && arrivalDateTime1.before(arrivalDateTime2));

        return isOverlap;
    }
    
    public Date combineDateTime(Date date, Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        return cal.getTime();
    }

}
