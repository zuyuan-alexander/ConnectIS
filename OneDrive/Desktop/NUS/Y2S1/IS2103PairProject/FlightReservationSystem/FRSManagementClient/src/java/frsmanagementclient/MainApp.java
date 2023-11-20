/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import entity.FlightSchedulePlan;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.ManagementSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.AircraftType;
import entity.CabinClass;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.Passenger;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
import util.exception.FlightScheduleNotFoundException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OverlappingScheduleException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFlightException;

/**
 *
 * @author alvintjw
 */
public class MainApp {
   private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private AircraftSessionBeanRemote aircraftSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBean;
    private FlightSessionBeanRemote flightSessionBean;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBean;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private ManagementSessionBeanRemote managementSessionBeanRemote;
    private Employee currentEmployee;

    
    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    

    public MainApp(EmployeeSessionBeanRemote employeeSessionBean, FlightScheduleSessionBeanRemote flightScheduleSessionBean, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean, AircraftSessionBeanRemote aircraftSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSessionBeanRemote flightSessionBean, ManagementSessionBeanRemote managementSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote) {
        this();
        this.employeeSessionBean = employeeSessionBean;
        this.flightSchedulePlanSessionBean = flightSchedulePlanSessionBean;
        this.flightScheduleSessionBean = flightScheduleSessionBean;
        this.aircraftSessionBeanRemote = aircraftSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSessionBean = flightSessionBean;
        this.managementSessionBeanRemote = managementSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;

    }
      
    
    public void runApp()
    {
        initFlightSchedulePlan();
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Mangagement System***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        
                      
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
     
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** FRS Management System: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployee = employeeSessionBean.employeeLogin(username, password);   
            if(currentEmployee.getUserRole().equals(EmployeeTypeEnum.SCHEDULE_MANAGER))
            {
                FlightOperationModuleMenuMain();
            } else if(currentEmployee.getUserRole().equals(EmployeeTypeEnum.ROUTE_MANAGER))
            {
                RoutePlannerMenuMain();
            } else if (currentEmployee.getUserRole().equals(EmployeeTypeEnum.FLEET_MANAGER))
            {
                FleetManagerMenuMain();
            } else if (currentEmployee.getUserRole().equals(EmployeeTypeEnum.SALES_MANAGER))
            {
                SalesManagerMenuMain();
            }
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
     
    private void FlightOperationModuleMenuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Schedule Manager Menu***\n");
            System.out.println("1: Create Flight");
            System.out.println("2: View All Flights");
            System.out.println("3: View Flight Details");
            System.out.println("4: Update Flight");
            System.out.println("5: Delete Flight");
            System.out.println("6: Create Flight Schedule Plan");
            System.out.println("7: View all Flight Schedule Plan");
            System.out.println("8: View Flight Schedule Plan Details");
            System.out.println("9: Update Flight Schedule Plan");
            System.out.println("10: Delete Flight Schedule Plan");
            System.out.println("11: Logout\n");
            response = 0;
            
            while(response < 1 || response > 11)
            {
                System.out.print("> ");

                response = scanner.nextInt();
                
                if(response == 1)
                {
                    doCreateFlight();
                } else if(response == 2)
                {
                    doViewAllFlights();
                } else if(response == 3)
                {
                    doViewFlightDetails();
                } else if(response == 4)
                {
                    doUpdateFlight();
                } else if(response == 5)
                {
                    doDeleteFlight();
                } else if(response == 6)
                {
                    doCreateFlightSchedulePlan();
                } else if(response == 7)
                {
                    doViewAllFlightSchedulePlans();
                } else if(response == 8)
                {
                    doViewFlightSchedulePlanDetails();
                } else if(response == 9)
                {
                    doUpdateFlightSchedulePlan();
                } else if(response == 10)
                {
                    doDeleteFlightSchedulePlan();
                }
                else if (response == 11)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 11)
            {
                break;
            }
        }
    }
   
    
    private void RoutePlannerMenuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Route Planner Menu***\n");
            System.out.println("1: Create Flight Route");
            System.out.println("2: View All Flight Routes");
            System.out.println("3: Delete Flight Route");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateFlightRoute();
                }
                else if (response == 2) 
                {
                    doViewAllFlightRoutes();
                }
                else if (response == 3) 
                {
                    doDeleteFlightRoute();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    private void FleetManagerMenuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Fleet Manager Menu***\n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configuration");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response ==1 ) {
                    doCreateAircraftConfiguration();
                } else if(response == 2) {
                    doViewAllAircraftConfigurations();
                } else if(response == 3) {
                    doViewAircraftConfigurationDetails();
                } else if(response == 4) {
                    break;
                } else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    
    private void SalesManagerMenuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Sales Manager Menu***\n");
            System.out.println("1: View Seats Inventory");
            System.out.println("2: View Flight Reservations");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doViewSeatsInventory();
                } 
                else if (response == 2) 
                {
                    doViewFlightReservations();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    
    public void doCreateFlight()
    {        
        Scanner sc = new Scanner(System.in);
        System.out.println("===== FRS Schedule Manager Menu: Create New Flight =====\n");
        System.out.print("Enter flight number > ");
        String flightNumber = sc.nextLine().trim();
        System.out.print("Enter origin IATA Airport Code > ");
        String origin = sc.nextLine().trim();
        System.out.print("Enter destination IATA Airport Code > ");
        String destination = sc.nextLine().trim();
        System.out.print("Enter aircraft configuration name > ");
        String aircraftConfigurationName = sc.nextLine().trim();
        System.out.print("Complimentary Flight (Y:Yes, N:No) > ");
        String response = sc.nextLine().trim();
        
        try {
            Flight flight = new Flight(flightNumber);
            FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOriginDestination(origin, destination);
            AircraftConfiguration acn = aircraftSessionBeanRemote.retrieveAircraftConfigurationByName(aircraftConfigurationName);
            
            Long flightId = flightSessionBean.createNewFlight(flight, flightRoute.getFlightRouteId(), acn.getAircraftConfigurationId());
                        
            if (response.equalsIgnoreCase("Y")) {
                System.out.print("Enter complementary flight number > ");
                String cFlightNumber = sc.nextLine().trim();
                System.out.print("Enter aircraft configuration name > ");
                String complementaryACN = sc.nextLine().trim();
                
                Long cFlightId = flightSessionBean.createComplementaryFlight(flightId, cFlightNumber, complementaryACN);
                
                System.out.println("Flight with Flight Id " + flightId + " has been successfully created!");
                System.out.println("Flight with Flight Id " + cFlightId + " has been successfully created!");
            } else {
                System.out.println("Flight with Flight Id " + flightId + " has been successfully created!");
            }
            
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
        } catch (FlightRouteDisabledException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
        
    } 
    
    
    public void doViewAllFlights() {
        System.out.println("===== View All Flights =====");
        List<Flight> flightList = flightSessionBean.viewAllFlight();
        
        if (flightList.isEmpty()) {
            System.out.println("No Flight is found!");
        }
        
        for (Flight flight : flightList) {
            if (!flight.getDisabledFlight()) {
                System.out.println("Flight Id: " + flight.getFlightId() + "; Flight Number: " + flight.getFlightNumber());
            }
        }
    }
    
    public void doViewFlightDetails() {
        System.out.println("===== View Flight Details =====");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter flight number > ");
        String flightNumber = sc.nextLine().trim();
       try {
           Flight flight = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
           System.out.println("Flight Id: " + flight.getFlightId());
           System.out.println("Flight Number: " + flight.getFlightNumber());
           System.out.println("Flight Route: " + flight.getFlightRoute().toString());
           System.out.println("Aircraft Configuration: " + flight.getAircraftConfiguration().getAircraftConfigurationName());
       } catch (FlightNotFoundException ex) {
           System.out.println(ex.getMessage() + "\n");
       }
    }
    
    public void doUpdateFlight() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== FRS Schedule Manager Menu: Update Flight =====\n");
        System.out.print("Enter flight number > ");
        String flightNumber = sc.nextLine().trim();
        System.out.print("Enter origin IATA Airport Code > ");
        String origin = sc.nextLine().trim();
        System.out.print("Enter destination IATA Airport Code > ");
        String destination = sc.nextLine().trim();
        System.out.print("Enter aircraft configuration name > ");
        String aircraftConfigurationName = sc.nextLine().trim();
        
        try {
            Flight flight = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
            FlightRoute flightRoute = flightRouteSessionBeanRemote.retrieveFlightRouteByOriginDestination(origin, destination);
            AircraftConfiguration acn = aircraftSessionBeanRemote.retrieveAircraftConfigurationByName(aircraftConfigurationName);
            flight.setFlightRoute(flightRoute);
            flight.setAircraftConfiguration(acn);

            Long flightToUpdateId = flightSessionBean.updateFlight(flight);
            
            System.out.println("Flight with Flight Id " + flightToUpdateId + " has been successfully updated!");
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (UpdateFlightException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } 
    }
    
    public void doDeleteFlight() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== FRS Schedule Manager Menu: Delete Flight =====\n");
        System.out.print("Enter flight id > ");
        Long flightId = sc.nextLong();
        
       try {
           flightSessionBean.deleteFlight(flightId);
           System.out.println("Flight with Flight Id " + flightId + " has been deleted!");
       } catch (FlightNotFoundException ex) {
           System.out.println(ex.getMessage() + "\n");
       }
    }
    
    private void doCreateFlightSchedulePlan()
    {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        Scanner sc = new Scanner(System.in);
        System.out.println("*** FRS Schedule Manager Menu: Create New Flight Schedule Plan ***\n");
        System.out.print("Enter Flight Number> ");  
        String flightnumber = sc.nextLine();
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber(flightnumber);
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
       
        Integer response = 0;
        while(true)
        {
             System.out.print("Select Flight SchedulePlanType (1: Single, 2: Multiple, 3: RecurrentNDay, 4: RecurrentWeekly)> ");
             response = sc.nextInt();
             sc.nextLine();
             if(response>=1 && response <=4)
             {
                 break;
             } else
             {
                System.out.println("Invalid option, please try again!\n");
             }
        }
       
       
        
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");
        
        if(response == 4)
        {
            newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
            System.out.print("Enter Day Of Week > ");
            String dayOfWeek = sc.nextLine();
            System.out.print("Enter Departure Time (eg: 9:00 AM)");
            String departureTimestr = sc.nextLine();
            System.out.print("Enter Start Date (dd MMM yy) > ");
            String startDateStr = sc.nextLine();
            System.out.print("Enter End Date (dd MMM yy) > ");
            String endDateStr = sc.nextLine();
            System.out.print("Enter Flight Duration (HH Hours mm Minutes) > ");
            String flightDurationStr = sc.nextLine();
            String layoverDurationStr = "";
            if(hasComFlight)
            {
                System.out.println("Enter Layover Duration for " + compf.getFlightNumber() + " (HH Hours mm Minutes) > ");
                layoverDurationStr = sc.nextLine();
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
                
                if(endDate.before(startDate))
                {
                    System.out.println("Input data validation error!: endDate must be after start Date");
                    return;
                }
                
                 Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);

                // Find the next occurrence of the specified day of the week
                int desiredDayOfWeek = convertToCalendarDayOfWeek(dayOfWeek);
                int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                int daysToAdd = (desiredDayOfWeek - currentDayOfWeek + 7) % 7;

                // Increment the start date to the next occurrence of the desired day of the week
                cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
                Date departureDate = cal.getTime();

                newFSP.setDayOfWeek(dayOfWeek);
                newFSP.setStartDate(startDate);
                newFSP.setEndDate(endDate);
                newFSP.setNdays(7);
                newFS.setDepartureDate(departureDate);
                newFS.setDepartureTime(departureTime);
                newFS.setEstimatedFlightDuration(flightDuration);
                newFS.calculateAndSetArrivalDateTime();
                
                Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(newFSP);
                if(!constraintViolations.isEmpty())
                {
                    showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                    return;
                } 
        
                
                for (FlightSchedule fs : ongoingfs) 
                {
                    if (checkOverlap(newFS, fs)) 
                    {       
                        throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");
                    }
                }
                
               
                
                Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
                Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
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
                    
                    constraintViolations = validator.validate(compFSP);
                    if(!constraintViolations.isEmpty())
                    {
                        showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                        return;
                    } 
                    
                 
                     compfspid = flightSchedulePlanSessionBean.createCompliMentaryFlightSchedulePlan(compf, compFSP, compFS, newfspid);
                     compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);            
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
                      System.out.println("old fsid " + newFS.getFlightscheduleid() + "with departure date " + newFS.getDepartureDate());

                    //add the main fs
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(newFS.getDepartureDate());
                    calendar.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                    newFS.setDepartureDate(calendar.getTime());
                    newFS.calculateAndSetArrivalDateTime(); 
                    
                    if(newFS.getDepartureDate().after(endDate))
                    {
                        break;
                    }
                    
                    newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
                    
                    
                       
                    //add the returning fs
                    if(hasComFlight)
                    {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(newFS.getDepartureDate());
                        calendar2.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                        compFS.setDepartureDate(calendar.getTime());
                        compFS.calculateAndSetArrivalDateTime(); 
                        compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);
                    
                    }
                 
                }
             
                for(CabinClass cabinClass : f.getAircraftConfiguration().getCabinClasses()) 
                {
                    System.out.print("Enter fare basis code for " + cabinClass.getCabinClassType() + " > ");
                    String fbc = sc.nextLine().trim();
                    System.out.print("Enter fare amount for " + cabinClass.getCabinClassType() + " > ");
                    BigDecimal fareAmount = sc.nextBigDecimal();
                    sc.nextLine();
                    Fare fare = new Fare(fbc, fareAmount, cabinClass.getCabinClassType());
                    fareSessionBeanRemote.createNewFare(fare, newfspid);
                }
                
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
            
            System.out.println("\n" + f.getFlightNumber() + ", " + newFSP.getScheduleType().name() + ": was created successfully");
        } else if(response == 3)
        {
            newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTNDAY);
            System.out.println("Enter NDay > ");
            Integer nDay = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Departure Time (eg: 9:00 AM) > ");
            String departureTimestr = sc.nextLine();
            System.out.print("Enter Start Date (dd MMM yy) > ");
            String startDateStr = sc.nextLine();
            System.out.print("Enter End Date (dd MMM yy) > ");
            String endDateStr = sc.nextLine();
            System.out.print("Enter Flight Duration (HH Hours mm Minutes) > ");
           
            String flightDurationStr = sc.nextLine();
             String layoverDurationStr = "";
            if(hasComFlight)
            {
                System.out.print("Enter Layover Duration for " + compf.getFlightNumber() + " (HH Hours mm Minutes) > ");
                layoverDurationStr = sc.nextLine();
            }
            
              Long compfspid = Long.MAX_VALUE; 
                Long compfsid = Long.MAX_VALUE;


            // Parse dates and FlightDuration
            try
            {
                Date startDate = dateFormat.parse(startDateStr);
                Date endDate = dateFormat.parse(endDateStr);
                Date flightDuration = timeFormat.parse(flightDurationStr);
                Date departureTime = departureFormat.parse(departureTimestr);
                Date layoverDuration = new Date();

                
                //newFSP.setDayOfWeek(dayOfWeek);
                newFSP.setStartDate(startDate);
                newFSP.setEndDate(endDate);
                newFSP.setNdays(nDay);
                newFS.setDepartureDate(startDate);
                newFS.setDepartureTime(departureTime);
                newFS.setEstimatedFlightDuration(flightDuration);
                newFS.calculateAndSetArrivalDateTime();
                
                Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(newFSP);
                if(!constraintViolations.isEmpty())
                {
                    showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                    return;
                } 
        
                for (FlightSchedule fs : ongoingfs) {
                    if (checkOverlap(newFS, fs)) {
                        
                        throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");
                    }
                }
                
                
                
                Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
                Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
                
                if(hasComFlight)
                {
                    layoverDuration = timeFormat.parse(layoverDurationStr);
                    FlightSchedule temp = new FlightSchedule();
                    temp.setEstimatedFlightDuration(layoverDuration);
                    temp.setDepartureDate(newFS.getArrivalDate());
                    temp.setDepartureTime(newFS.getArrivalTime());
                    temp.calculateAndSetArrivalDateTime();
    
                    compFSP.setScheduleType(ScheduleTypeEnum.RECURRENTNDAY);
                    compFSP.setStartDate(startDate);
                    compFSP.setEndDate(endDate);
                    compFSP.setNdays(nDay);
                    compFS.setDepartureDate(temp.getArrivalDate());
                    compFS.setDepartureTime(temp.getArrivalTime());
                    compFS.setEstimatedFlightDuration(flightDuration);
                    compFS.calculateAndSetArrivalDateTime();
                    
                    constraintViolations = validator.validate(compFSP);
                    if(!constraintViolations.isEmpty())
                    {
                        showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                        return;
                    } 
                 
                     compfspid = flightSchedulePlanSessionBean.createCompliMentaryFlightSchedulePlan(compf, compFSP, compFS, newfspid);
                     compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);
                    
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
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(newFS.getDepartureDate());
                    calendar.add(Calendar.DAY_OF_MONTH, nDay); // Increment by 7 days
                    newFS.setDepartureDate(calendar.getTime());
                    newFS.calculateAndSetArrivalDateTime(); 
                    newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
                    
                    
                    
                    if(hasComFlight)
                    {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(newFS.getDepartureDate());
                        calendar2.add(Calendar.DAY_OF_MONTH, nDay); // Increment by 7 days
                        compFS.setDepartureDate(calendar.getTime());
                        compFS.calculateAndSetArrivalDateTime(); 
                        compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);
                    
                    }
                }
                
                for(CabinClass cabinClass : f.getAircraftConfiguration().getCabinClasses()) 
                {
                    System.out.print("Enter fare basis code for " + cabinClass.getCabinClassType() + " > ");
                    String fbc = sc.nextLine().trim();
                    System.out.print("Enter fare amount for " + cabinClass.getCabinClassType() + " > ");
                    BigDecimal fareAmount = sc.nextBigDecimal();
                    sc.nextLine();
                    Fare fare = new Fare(fbc, fareAmount, cabinClass.getCabinClassType());
                    fareSessionBeanRemote.createNewFare(fare, newfspid);
                }
            } catch (OverlappingScheduleException ex)
            {
                System.out.println(ex.getMessage());
            } catch (ParseException ex)
            {
                ex.printStackTrace();
            }catch (InputDataValidationException ex)
            {
                System.out.println(ex.getMessage());
            } catch (FlightDisabledException ex) {
                System.out.println(ex.getMessage());
            }
             System.out.println(f.getFlightNumber() + ", " + newFSP.getScheduleType().name() + ": was created successfully");  
        } else if(response == 2)
        {
            newFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
            System.out.print("Enter Number of Flight Schedules> ");
            Integer numFS = sc.nextInt();
            sc.nextLine();

            try {
                Date departureDate;
                Date departureTime;
                Date flightDuration;
                Date layoverDuration = new Date();

                // Prompt and create the first flight schedule
                System.out.print("Enter Departure Date for FlightSchedule 1: (dd MMM yy) > ");
                String departuredatestr = sc.nextLine();
                System.out.print("Enter Departure Time for FlightSchedule 1: (eg: 9:00 AM) > ");
                String departureTimestr = sc.nextLine();
                System.out.print("Enter Flight Duration: (HH Hours mm Minutes) > ");
                String layoverDurationStr = "";
                String flightDurationStr = sc.nextLine();
                if(hasComFlight)
                {
                    System.out.print("Enter Layover Duration for " + compf.getFlightNumber() + " (HH Hours mm Minutes) > ");
                    layoverDurationStr = sc.nextLine();
                }

                Long compfspid = Long.MAX_VALUE; 
                Long compfsid = Long.MAX_VALUE;

                departureDate = dateFormat.parse(departuredatestr);
                departureTime = departureFormat.parse(departureTimestr);
                flightDuration = timeFormat.parse(flightDurationStr);

                newFS.setDepartureDate(departureDate);
                newFS.setDepartureTime(departureTime);
                newFS.setEstimatedFlightDuration(flightDuration);
                newFS.calculateAndSetArrivalDateTime();
                
                Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(newFSP);
                if(!constraintViolations.isEmpty())
                {
                    showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                    return;
                } 
        
                for (FlightSchedule fs : ongoingfs) 
                {
                    if (checkOverlap(newFS, fs)) 
                    {

                        throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");

                    }
                }

                Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
                Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
                
                if(hasComFlight)
                {
                    layoverDuration = timeFormat.parse(layoverDurationStr);
                    FlightSchedule temp = new FlightSchedule();
                    temp.setEstimatedFlightDuration(layoverDuration);
                    temp.setDepartureDate(newFS.getArrivalDate());
                    temp.setDepartureTime(newFS.getArrivalTime());
                    temp.calculateAndSetArrivalDateTime();
    
                    compFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                    compFS.setDepartureDate(temp.getArrivalDate());
                    compFS.setDepartureTime(temp.getArrivalTime());
                    compFS.setEstimatedFlightDuration(flightDuration);
                    compFS.calculateAndSetArrivalDateTime();
                    
                 
                    compfspid = flightSchedulePlanSessionBean.createCompliMentaryFlightSchedulePlan(compf, compFSP, compFS, newfspid);
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);
                    
                }

                // Create additional flight schedules based on user input
                for (int i = 2; i <= numFS; i++)
                {
                    System.out.println("Enter Departure Date for FlightSchedule " + i + ": (dd MMM yy) > ");
                    departuredatestr = sc.nextLine();
                    System.out.println("Enter Departure Time for FlightSchedule " + i + ": (eg: 9:00 AM) > ");
                    departureTimestr = sc.nextLine();
                    

                    departureDate = dateFormat.parse(departuredatestr);
                    departureTime = departureFormat.parse(departureTimestr);
                    flightDuration = timeFormat.parse(flightDurationStr);

                    newFS.setDepartureDate(departureDate);
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

                    newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
                    
                    if(hasComFlight)
                    {
                        layoverDuration = timeFormat.parse(layoverDurationStr);
                        FlightSchedule temp = new FlightSchedule();
                        temp.setEstimatedFlightDuration(layoverDuration);
                        temp.setDepartureDate(newFS.getArrivalDate());
                        temp.setDepartureTime(newFS.getArrivalTime());
                        temp.calculateAndSetArrivalDateTime();

                        compFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                        compFS.setDepartureDate(temp.getArrivalDate());
                        compFS.setDepartureTime(temp.getArrivalTime());
                        compFS.setEstimatedFlightDuration(flightDuration);
                        compFS.calculateAndSetArrivalDateTime();
                        
                        
                        constraintViolations = validator.validate(compFSP);
                        if(!constraintViolations.isEmpty())
                        {
                            showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                            return;
                        } 


                        compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                        compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

                    }
                }
                
                for(CabinClass cabinClass : f.getAircraftConfiguration().getCabinClasses()) 
                {
                    System.out.print("Enter fare basis code for " + cabinClass.getCabinClassType() + " > ");
                    String fbc = sc.nextLine().trim();
                    System.out.print("Enter fare amount for " + cabinClass.getCabinClassType() + " > ");
                    BigDecimal fareAmount = sc.nextBigDecimal();
                    sc.nextLine();
                    Fare fare = new Fare(fbc, fareAmount, cabinClass.getCabinClassType());
                    fareSessionBeanRemote.createNewFare(fare, newfspid);
                }
            } catch (OverlappingScheduleException ex) {
                System.out.println(ex.getMessage());
            } catch (ParseException ex) {
                ex.printStackTrace();

            }catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage());
            } catch (FlightDisabledException ex) {
                System.out.println(ex.getMessage());
            }
            
             System.out.println(f.getFlightNumber() + ", " + newFSP.getScheduleType().name() + ": was created successfully");
        } else if(response == 1)
        {
            newFSP.setScheduleType(ScheduleTypeEnum.SINGLE);
            try {
                Date departureDate;
                Date departureTime;
                Date flightDuration;
                Date layoverDuration;

                // Prompt and create the first flight schedule
                System.out.println("Enter Departure Date for FlightSchedule 1: (dd MMM yy) > ");
                String departuredatestr = sc.nextLine();
                System.out.println("Enter Departure Time for FlightSchedule 1: (eg: 9:00 AM) > ");
                String departureTimestr = sc.nextLine();
                System.out.println("Enter Flight Duration: (HH Hours mm Minutes)");
                String flightDurationStr = sc.nextLine();
                String layoverDurationStr = "";
                if(hasComFlight)
                {
                    System.out.println("Enter Layover Duration for " + compf.getFlightNumber() + " (HH Hours mm Minutes) > ");
                    layoverDurationStr = sc.nextLine();
                }

                Long compfspid = Long.MAX_VALUE; 
                Long compfsid = Long.MAX_VALUE;

                departureDate = dateFormat.parse(departuredatestr);
                departureTime = departureFormat.parse(departureTimestr);
                flightDuration = timeFormat.parse(flightDurationStr);

                newFS.setDepartureDate(departureDate);
                newFS.setDepartureTime(departureTime);
                newFS.setEstimatedFlightDuration(flightDuration);
                newFS.calculateAndSetArrivalDateTime();
                
                Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(newFSP);
                if(!constraintViolations.isEmpty())
                {
                    showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                    return;
                }
                
                
                for (FlightSchedule fs : ongoingfs) {
                        if (checkOverlap(newFS, fs)) {

                            throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");
                            
                        }
                    }

                Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
                Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
                
                if(hasComFlight)
                {
                    layoverDuration = timeFormat.parse(layoverDurationStr);
                    FlightSchedule temp = new FlightSchedule();
                    temp.setEstimatedFlightDuration(layoverDuration);
                    temp.setDepartureDate(newFS.getArrivalDate());
                    temp.setDepartureTime(newFS.getArrivalTime());
                    temp.calculateAndSetArrivalDateTime();
    
                    compFSP.setScheduleType(ScheduleTypeEnum.SINGLE);
                    compFS.setDepartureDate(temp.getArrivalDate());
                    compFS.setDepartureTime(temp.getArrivalTime());
                    compFS.setEstimatedFlightDuration(flightDuration);
                    compFS.calculateAndSetArrivalDateTime();
                    
                   constraintViolations = validator.validate(compFSP);
                    if(!constraintViolations.isEmpty())
                    {
                        showInputDataValidationErrorsForFlightSchedulePlan(constraintViolations);
                        return;
                    } 
                 
                    compfspid = flightSchedulePlanSessionBean.createCompliMentaryFlightSchedulePlan(compf, compFSP, compFS, newfspid);
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);
                    
                }
                for(CabinClass cabinClass : f.getAircraftConfiguration().getCabinClasses()) 
                {
                    System.out.print("Enter fare basis code for " + cabinClass.getCabinClassType() + " > ");
                    String fbc = sc.nextLine().trim();
                    System.out.print("Enter fare amount for " + cabinClass.getCabinClassType() + " > ");
                    BigDecimal fareAmount = sc.nextBigDecimal();
                    sc.nextLine();
                    Fare fare = new Fare(fbc, fareAmount, cabinClass.getCabinClassType());
                    fareSessionBeanRemote.createNewFare(fare, newfspid);
                }
             
            } catch (OverlappingScheduleException ex) {
                System.out.println(ex.getMessage());
            } catch (ParseException ex) {
                ex.printStackTrace();

            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage());
            } 
             catch (FlightDisabledException ex) {
                System.out.println(ex.getMessage());
            }

            
            System.out.println(f.getFlightNumber() + ", " + newFSP.getScheduleType().name() + ": was created successfully");
      
        }
        
        //System.out.println("Flight Schedule Plan with Id " + newFSP.getFlightscheduleplanid() + " has been successfully created!");
    } 
    
    public void doViewAllFlightSchedulePlans()
    {
        System.out.println("\n\n*** View All Flight Schedule Plan *** \n");
        List<FlightSchedulePlan> fspList = flightSchedulePlanSessionBean.retrieveAllFlightSchedulePlan();

        FlightSchedulePlan currfsp = new FlightSchedulePlan();
        String fspText = "List of Flight Schedule Plans:\n";
   
        for(int i = 1; i < fspList.size() + 1 ; i++) 
        {
            currfsp = fspList.get(i-1);
            if(currfsp.getScheduleType().equals(ScheduleTypeEnum.SINGLE))
            {
                 fspText += i + ": Single Plan" + " (" + currfsp.getFlight().getFlightNumber() + ") " + "FSP id: " + currfsp.getFlightscheduleplanid() + "\n";
            } else if(currfsp.getScheduleType().equals(ScheduleTypeEnum.MULTIPLE))
            {
                 fspText += i + ": Multiple Plan" + " (" + currfsp.getFlight().getFlightNumber() + ") " + "FSP id: " + currfsp.getFlightscheduleplanid() + "\n";
            } else if(currfsp.getScheduleType().equals(ScheduleTypeEnum.RECURRENTNDAY))
            {
                 fspText += i + ": Recurrent Plan(N-Day)" + " (" + currfsp.getFlight().getFlightNumber() + ") " + "FSP id: " + currfsp.getFlightscheduleplanid() + "\n";
            } else if(currfsp.getScheduleType().equals(ScheduleTypeEnum.RECURRENTWEEKLY))
            {
                 fspText += i + ": Recurrent Plan(Weekly)" + " (" + currfsp.getFlight().getFlightNumber() + ") " + "FSP id: " + currfsp.getFlightscheduleplanid() + "\n";
            }
        }
        
        System.out.print(fspText);
   
    }
    
    public void doViewFlightSchedulePlanDetails() {
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            System.out.println("\n\n*** View Flight Schedule Plan Details *** \n");
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter flight number > ");
            String flightNumber = sc.nextLine().trim();
            Flight f = new Flight();
            try
            {
               f = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
            } catch(FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
            }
           
            List<FlightSchedulePlan> fsplist = f.getFlightscheduleplans();
            
            
            
            System.out.println("Flight Aircraft Configuration: " + f.getAircraftConfiguration());
            System.out.println("Flight Route: " + f.getFlightRoute().getOrigin() + " -> " + f.getFlightRoute().getDestination());
            System.out.println();
            for(FlightSchedulePlan fsp : fsplist )
            {
                System.out.println("Flight Schedule Plan Id: " + fsp.getFlightscheduleplanid());
                System.out.println("FLight Schedule Plan Type: " + fsp.getScheduleType());
                System.out.println();
            }
            
            System.out.println();
            System.out.println("Enter Flight Schedule Plan Id to view: ");
            Long chosenfspid = sc.nextLong();
            sc.nextLine();
            FlightSchedulePlan chosenfsp = new FlightSchedulePlan();
            try
            {
                chosenfsp = flightSchedulePlanSessionBean.retrieveFSPfByFSPId(chosenfspid);
            } catch (FlightSchedulePlanNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
          
            List<FlightSchedule> fsList = chosenfsp.getFlightschedules();
            List<Fare> fares = chosenfsp.getFares();
            
            
            for (FlightSchedule fs : fsList) {
                System.out.println("Flight Schedule Id: " + fs.getFlightscheduleid() + "; Departure Date: " + dateFormat.format(fs.getDepartureDate()) + 
                        "; Departure Time: " + timeFormat.format(fs.getDepartureTime()) + "; Flight Duration: " + timeFormat.format(fs.getEstimatedFlightDuration()));
            }
            
            //System.out.println(fsList.size());
            System.out.println();
            // print layout
            
            
            for (Fare fare : fares) {
                System.out.println("Cabin Class Type: " + fare.getCabinClassType() + "; Fare basis code: " + fare.getFareBasicCode() + "; Fare Amount: " + fare.getFareAmount());
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

    // Helper method to combine date and time
    private Date combineDateTime(Date date, Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(time);

        cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        return cal.getTime();
    }
    
    private int convertToCalendarDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case "sunday":
                return Calendar.SUNDAY;
            case "monday":
                return Calendar.MONDAY;
            case "tuesday":
                return Calendar.TUESDAY;
            case "wednesday":
                return Calendar.WEDNESDAY;
            case "thursday":
                return Calendar.THURSDAY;
            case "friday":
                return Calendar.FRIDAY;
            case "saturday":
                return Calendar.SATURDAY;
            default:
                throw new IllegalArgumentException("Invalid day of week input");
        }
    }


    
    public void doUpdateFlightSchedulePlan() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Scanner sc = new Scanner(System.in);
        doViewAllFlightSchedulePlans();
        FlightSchedulePlan newfsp = new FlightSchedulePlan();
         FlightSchedulePlan currfsp = new FlightSchedulePlan();
        System.out.println("\n\n*** Update A Flight Schedule Plan *** \n");
        System.out.println("Which flight schedule plan ID would you like to update?");
        Long flightPlanId = sc.nextLong();
        sc.nextLine();
        
        try
        {
            currfsp = flightSchedulePlanSessionBean.retrieveFSPfByFSPId(flightPlanId);
        } catch (FlightSchedulePlanNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        Flight f = currfsp.getFlight();
        List<Fare> listOffares = new ArrayList();
       
        System.out.println("===== FRS Schedule Manager Menu: Update Flight Schedule Plan =====\n");      
        for(Fare currfare : currfsp.getFares()) 
        {
            System.out.println("Current fare amount for " + currfare.getCabinClassType().name() + " > " + currfare.getFareAmount());            
        }
        
        for(Fare fare : currfsp.getFares()) 
        {
            System.out.print("Enter New fare amount for " + fare.getCabinClassType().name() + " > ");
            BigDecimal fareAmount = sc.nextBigDecimal();
            sc.nextLine();
            Fare newfare = new Fare("farebc", fareAmount, fare.getCabinClassType());
            fareSessionBeanRemote.createNewFare(newfare, currfsp.getFlightscheduleplanid());
            
            
        }
        
        try
        {
            flightSchedulePlanSessionBean.updateFlightSchedulePlan(currfsp.getFares(), currfsp.getFlightscheduleplanid());
        } catch(FlightDisabledException | FlightSchedulePlanNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public void doDeleteFlightSchedulePlan()
    {
        Scanner sc = new Scanner(System.in);
        doViewAllFlightSchedulePlans();
         FlightSchedulePlan currfsp = new FlightSchedulePlan();
        System.out.println("\n\n*** Delete A Flight Schedule Plan *** \n");
        System.out.println("Which flight schedule plan ID would you like to delete?");
        Long flightPlanId = sc.nextLong();
        sc.nextLine();
        try
        {
            currfsp = flightSchedulePlanSessionBean.retrieveFSPfByFSPId(flightPlanId);
            flightSchedulePlanSessionBean.deleteFlightSchedulePlan(flightPlanId);
        } catch (FlightSchedulePlanNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        
    }

    public void doCreateAircraftConfiguration() {
        System.out.println("====== Create Aircraft Configuration =====");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter aircraft type > ");
        String aircraftTypeName = scanner.nextLine().trim();
        System.out.print("Enter aircraft configuration name > ");
        String aircraftConfigurationName = scanner.nextLine().trim();
        System.out.print("Enter number of cabin classes > ");
        Integer numOfCabinClasses = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter maximum capacity > ");
        Integer maximumCapacity = scanner.nextInt();
        scanner.nextLine();
        
        List<CabinClass> cabinClassList = new ArrayList<>();
        Integer maxCapacityLimit = 0;
        
        for (int i=1; i<=numOfCabinClasses; i++) {
            System.out.println("*** Cabin Class " + i + " ***");
            System.out.print("Enter cabin class type (F: First, J: Business, W: Premium Economy, Y: Economy) > ");
            String cabinClassTypeStr = scanner.nextLine().trim();
            
            CabinClassTypeEnum cabinClassType = null;
            if (cabinClassTypeStr.equalsIgnoreCase("F")) {
                cabinClassType = CabinClassTypeEnum.F;
            } else if (cabinClassTypeStr.equalsIgnoreCase("J")) {
                cabinClassType = CabinClassTypeEnum.J;
            } else if (cabinClassTypeStr.equalsIgnoreCase("W")) {
                cabinClassType = CabinClassTypeEnum.W;
            } else if (cabinClassTypeStr.equalsIgnoreCase("Y")) {
                cabinClassType = CabinClassTypeEnum.Y;
            }
            
            System.out.print("Enter number of aisles > ");
            Integer numOfAisles = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter number of rows > ");
            Integer numOfRows = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter number of seat abreast > ");
            Integer numOfSeatAbreast = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter seat configuration > ");
            String seatConfiguration = scanner.nextLine().trim();
            System.out.print("Enter maximum capacity of the cabin class > ");
            Integer maxCapacity = scanner.nextInt();
            scanner.nextLine();
            
            maxCapacityLimit += maxCapacity;
            if (maxCapacityLimit > maximumCapacity) {
                System.out.println("Total capacity of all cabin classes exceeds the maximum capacity of the aircraft configuration!");
                return;
            }
            
            CabinClass cabinClass = new CabinClass(cabinClassType, numOfAisles, numOfRows, numOfSeatAbreast, seatConfiguration, maxCapacity);
            cabinClassList.add(cabinClass);
        }
        
        try {
            AircraftConfiguration aircraftConfiguration = new AircraftConfiguration(aircraftConfigurationName, numOfCabinClasses, maximumCapacity);
            AircraftType aircraftType = aircraftSessionBeanRemote.retrieveAircraftByAircraftTypeName(aircraftTypeName);
        
            Long aircraftConfigurationId = aircraftSessionBeanRemote.createAircraftConfiguration(aircraftConfiguration, aircraftType, cabinClassList);
            
            System.out.println("Aircraft Configuration with Id " + aircraftConfigurationId + " has been successfully created!");
        } catch(AircraftTypeNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } 
    }
    
    public void doViewAllAircraftConfigurations() {
        System.out.println("====== View All Aircraft Configuration =====");
        List<AircraftConfiguration> aircraftConfigurationList = aircraftSessionBeanRemote.viewAllAircraftConfigurations();
        
        if (aircraftConfigurationList.isEmpty()) {
            System.out.println("No Aircraft Configuration is found!");
        }
        
        for (AircraftConfiguration ac : aircraftConfigurationList) {
            System.out.println("Aircraft Configuration Id: " + ac.getAircraftConfigurationId() + "; Name: " + ac.getAircraftConfigurationName() + "; Aircraft Type: " + ac.getAircraftType());
        }
    }
    
    public void doViewAircraftConfigurationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====== View Aircraft Configuration Details =====");
        System.out.print("Enter aircraft configuration name > ");
        String acn = scanner.nextLine().trim();
        
        try {
            AircraftConfiguration ac = aircraftSessionBeanRemote.retrieveAircraftConfigurationByName(acn);
            System.out.println("Aircraft Configuration Id : " + ac.getAircraftConfigurationId());
            System.out.println("Aircraft Configuration Name : " + ac.getAircraftConfigurationName());
            System.out.println("Number of Cabin Classes : " + ac.getNumOfCabinClass());
            System.out.println("Maximum Capacity : " + ac.getMaximumCapacity());
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }

    }
     
    
    public void doCreateFlightRoute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====== Create Flight Route =====");
        System.out.print("Enter Origin IATA Airport Code > ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter Destination IATA Airport Code > ");
        String destination = scanner.nextLine().trim();
        System.out.print("Do you want to create a complementary return route? (Y: Yes, N: No) > ");
        String returnRoute = scanner.nextLine().trim();
        
        FlightRoute flightRoute = new FlightRoute(origin, destination);
        
        if (returnRoute.equalsIgnoreCase("Y")) {
            flightRoute.setReturnFlight(Boolean.TRUE);
        } else if (returnRoute.equalsIgnoreCase("N")) {
            flightRoute.setReturnFlight(Boolean.FALSE);
        }
        
        try 
        {
            Long id = flightRouteSessionBeanRemote.createFlightRoute(flightRoute);
            System.out.println("Flight Route with Flight Route Id " + (id-1) + " has been successfully created!");
            System.out.println("Flight Route with Flight Route Id " + id + " has been successfully created!");
            
        } catch (AirportNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightRouteAlreadyExistedException ex) 
        {
            System.out.println(ex.getMessage() + "\n");
        } catch (UnknownPersistenceException ex) 
        {
            System.out.println(ex.getMessage() + "\n");
        } catch (InputDataValidationException ex) 
        {
            System.out.println(ex.getMessage() + "\n");
        }
        
    }
    
    public void doViewAllFlightRoutes() {
        System.out.println("====== View All Flight Routes =====");
        List<FlightRoute> flightRouteList = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        
        if (flightRouteList.isEmpty()) {
            System.out.println("No Flight Route is found!");
        }
        
        for (FlightRoute fr : flightRouteList) {
            if (!fr.getDisabledFlight()) {
                System.out.println("Flight Route Id: " + fr.getFlightRouteId() + " with Origin " + fr.getOrigin() + " and Destination " + fr.getDestination());
            }
        }
    }
    
    public void doDeleteFlightRoute() {
        System.out.println("====== Delete Flight Route =====");
        Scanner scanner = new Scanner(System.in);
        //System.out.print("Enter flight route id > ");
        //Long id = scanner.nextLong();
        System.out.print("Enter origin IATA Airport Code > ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter destination IATA Airport Code > ");
        String destination = scanner.nextLine().trim();
        try {
            FlightRoute fr = flightRouteSessionBeanRemote.retrieveFlightRouteByOriginDestination(origin, destination);
            flightRouteSessionBeanRemote.deleteFlightRoute(fr.getFlightRouteId());
            System.out.println("Flight Route with Id " + fr.getFlightRouteId() + " has been successfully deleted!");
        } catch (FlightRouteNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    

    public void doViewSeatsInventory() {
        try {
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
            System.out.println("===== View Seats Inventory =====");
            System.out.print("Enter flight number > ");
            String flightNumber = sc.nextLine().trim();
            System.out.print("Enter date (dd MMM yy) > ");
            String dateStr = sc.nextLine().trim();
            Date date = dateFormat.parse(dateStr);
            List<FlightSchedule> fsList = flightScheduleSessionBean.retrieveFlightScheduleByDate(date, flightNumber);
            
            if (fsList.isEmpty()) {
                System.out.println("No Flight Schedules found!");
            }
            
            for (FlightSchedule fs : fsList) {
                Long flightScheduleId = fs.getFlightscheduleid();
                System.out.println("*** Flight Schedule Id " + flightScheduleId + " ***");
                Flight flight = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
                FlightSchedule flightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(flightScheduleId);
                List<CabinClass> ccList = flight.getAircraftConfiguration().getCabinClasses();
                Integer totalNumOfReservedSeats = 0;
                Integer totalNumOfAvailableSeats = 0;
                Integer totalNumOfBalanceSeats = 0;

                for (CabinClass cabinClass : ccList) {
                    System.out.println("For Cabin Class: " + cabinClass.getCabinClassType());

                    Integer numOfReservedSeats = managementSessionBeanRemote.viewSeatsInventory(cabinClass, flightSchedule).size();
                    totalNumOfReservedSeats += numOfReservedSeats;

                    Integer numOfAvailableSeats = (cabinClass.getNumOfRows() * cabinClass.getNumOfSeatsAbreast()) - numOfReservedSeats;
                    totalNumOfAvailableSeats += numOfAvailableSeats;

                    Integer numOfBalanceSeats = numOfAvailableSeats - numOfReservedSeats;
                    totalNumOfBalanceSeats += numOfBalanceSeats;

                    System.out.println("Number of Reserved Seats: " + numOfReservedSeats + "; Number of Available Seats: " + numOfAvailableSeats + "; Number of Balance Seats: " + numOfBalanceSeats);

                    System.out.println();
                }

                System.out.println("Total Number of Reserved Seats: " + totalNumOfReservedSeats + "; Total Number of Available Seats: " + totalNumOfAvailableSeats + "; Total Number of Balance Seats: " + totalNumOfBalanceSeats + "\n\n");
                
            }
            
        } catch (ParseException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightScheduleNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void doViewFlightReservations() {
        try {
            Scanner sc = new Scanner(System.in);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
            System.out.println("===== View Flight Reservations =====");
            System.out.print("Enter flight number > ");
            String flightNumber = sc.nextLine().trim();
            System.out.print("Enter date (dd MMM yy) > ");
            String dateStr = sc.nextLine().trim();
            Date date = dateFormat.parse(dateStr);
            List<FlightSchedule> fsList = flightScheduleSessionBean.retrieveFlightScheduleByDate(date, flightNumber);
            
            if (fsList.isEmpty()) {
                System.out.println("No Flight Schedules found!");
            }
            
            for (FlightSchedule fs : fsList) {
                Long flightScheduleId = fs.getFlightscheduleid();
                System.out.println("*** Flight Schedule Id " + flightScheduleId + " ***");
                Flight flight = flightSessionBean.retrieveFlightByFlightNumber(flightNumber);
                FlightSchedule flightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(flightScheduleId);
                List<CabinClass> ccList = flight.getAircraftConfiguration().getCabinClasses();
                FlightSchedulePlan fsp = flightSchedulePlanSessionBean.retrieveFSPfByFSPId(flightSchedule.getFlightSchedulePlan().getFlightscheduleplanid());
                for (CabinClass cabinClass : ccList) {
                    System.out.println("For Cabin Class: " + cabinClass.getCabinClassType());
                    List<Passenger> passengers = managementSessionBeanRemote.viewSeatsInventory(cabinClass, flightSchedule);
                    Fare fare = fareSessionBeanRemote.retrieveFareAmountByCabinClassType(fsp.getFares(), cabinClass);
                    
                    if (passengers.size() == 0) {
                        System.out.println("Passenger List is empty!");
                    }

                    for (Passenger passenger : passengers) {
                        System.out.println("Seat Number: " + passenger.getSeat().toString() + "; Passenger: " + passenger.toString() + "; Fare basis code: " + fare.getFareBasicCode());
                    }
                    System.out.println();
                }
                System.out.println("\n\n");
            }
            
        } catch (ParseException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightScheduleNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        } 
    }
    
    public void initFlightSchedulePlan() {
        initFlightSchedulePlan1();
        initFlightSchedulePlan2();
        initFlightSchedulePlan3();
        initFlightSchedulePlan4();
        initFlightSchedulePlan5();
        initFlightSchedulePlan6();
    }
    
    public void initFlightSchedulePlan1() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber("ML711");
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");

        newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
        String dayOfWeek = "Monday";
        String departureTimestr = "9:00 AM";
        String startDateStr = "01 Dec 23";
        String endDateStr = "31 Dec 23";
        String flightDurationStr = "14 Hours 00 Minutes";
        String layoverDurationStr = "";
        if(hasComFlight)
        {
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

            Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
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


                 compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                 compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);            
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
                newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);


                //add the returning fs
                if(hasComFlight)
                {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(newFS.getDepartureDate());
                    calendar2.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                    compFS.setDepartureDate(calendar.getTime());
                    compFS.calculateAndSetArrivalDateTime(); 
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

                }

            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(6000), CabinClassTypeEnum.F);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(3000), CabinClassTypeEnum.J);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(1000), CabinClassTypeEnum.Y);
            fareSessionBeanRemote.createNewFare(fare, newfspid);

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
    
    public void initFlightSchedulePlan2() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber("ML611");
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");

        newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
        String dayOfWeek = "Sunday";
        String departureTimestr = "12:00 PM";
        String startDateStr = "01 Dec 23";
        String endDateStr = "31 Dec 23";
        String flightDurationStr = "8 Hours 00 Minutes";
        String layoverDurationStr = "";
        if(hasComFlight)
        {
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

            Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
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


                 compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                 compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);            
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
                newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);


                //add the returning fs
                if(hasComFlight)
                {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(newFS.getDepartureDate());
                    calendar2.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                    compFS.setDepartureDate(calendar.getTime());
                    compFS.calculateAndSetArrivalDateTime(); 
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

                }

            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(3000), CabinClassTypeEnum.F);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(1500), CabinClassTypeEnum.J);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(500), CabinClassTypeEnum.Y);
            fareSessionBeanRemote.createNewFare(fare, newfspid);

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
    
    public void initFlightSchedulePlan3() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber("ML621");
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");

        newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
        String dayOfWeek = "Tuesday";
        String departureTimestr = "10:00 AM";
        String startDateStr = "01 Dec 23";
        String endDateStr = "31 Dec 23";
        String flightDurationStr = "8 Hours 00 Minutes";
        String layoverDurationStr = "";
        if(hasComFlight)
        {
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

            Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
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


                 compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                 compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);            
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
                newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);


                //add the returning fs
                if(hasComFlight)
                {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(newFS.getDepartureDate());
                    calendar2.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                    compFS.setDepartureDate(calendar.getTime());
                    compFS.calculateAndSetArrivalDateTime(); 
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

                }

            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(6000), CabinClassTypeEnum.F);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(3000), CabinClassTypeEnum.J);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(1000), CabinClassTypeEnum.Y);
            fareSessionBeanRemote.createNewFare(fare, newfspid);

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
    
        public void initFlightSchedulePlan4() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber("ML311");
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");

        newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTWEEKLY);
        String dayOfWeek = "Monday";
        String departureTimestr = "10:00 AM";
        String startDateStr = "01 Dec 23";
        String endDateStr = "31 Dec 23";
        String flightDurationStr = "6 Hours 30 Minutes";
        String layoverDurationStr = "";
        if(hasComFlight)
        {
            layoverDurationStr = "3 Hours 00 Minutes";
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

            Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);
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


                 compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                 compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);            
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
                newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);


                //add the returning fs
                if(hasComFlight)
                {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(newFS.getDepartureDate());
                    calendar2.add(Calendar.DAY_OF_MONTH, 7); // Increment by 7 days
                    compFS.setDepartureDate(calendar.getTime());
                    compFS.calculateAndSetArrivalDateTime(); 
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

                }

            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(3100), CabinClassTypeEnum.F);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(1600), CabinClassTypeEnum.J);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(600), CabinClassTypeEnum.Y);
            fareSessionBeanRemote.createNewFare(fare, newfspid);

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
        
    public void initFlightSchedulePlan5() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber("ML411");
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");
        
        newFSP.setScheduleType(ScheduleTypeEnum.RECURRENTNDAY);
        Integer nDay = 2;
        String departureTimestr = "1:00 PM";
        String startDateStr = "01 Dec 23";
        String endDateStr = "31 Dec 23";
        String flightDurationStr = "4 Hours 00 Minutes";
        String layoverDurationStr = "";
        if(hasComFlight)
        {
            layoverDurationStr = "4 Hours 00 Minutes";
        }

        Long compfspid = Long.MAX_VALUE; 
        Long compfsid = Long.MAX_VALUE;


        // Parse dates and FlightDuration
        try
        {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            Date flightDuration = timeFormat.parse(flightDurationStr);
            Date departureTime = departureFormat.parse(departureTimestr);
            Date layoverDuration = new Date();


            //newFSP.setDayOfWeek(dayOfWeek);
            newFSP.setStartDate(startDate);
            newFSP.setEndDate(endDate);
            newFSP.setNdays(nDay);
            newFS.setDepartureDate(startDate);
            newFS.setDepartureTime(departureTime);
            newFS.setEstimatedFlightDuration(flightDuration);
            newFS.calculateAndSetArrivalDateTime();
            for (FlightSchedule fs : ongoingfs) {
                if (checkOverlap(newFS, fs)) {

                    throw new OverlappingScheduleException("Overlap found on the " + newFS.getDepartureDate() + " Cannot create new Flight Schedule Plan.");
                }
            }



            Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);

            if(hasComFlight)
            {
                layoverDuration = timeFormat.parse(layoverDurationStr);
                FlightSchedule temp = new FlightSchedule();
                temp.setEstimatedFlightDuration(layoverDuration);
                temp.setDepartureDate(newFS.getArrivalDate());
                temp.setDepartureTime(newFS.getArrivalTime());
                temp.calculateAndSetArrivalDateTime();

                compFSP.setScheduleType(ScheduleTypeEnum.RECURRENTNDAY);
                compFSP.setStartDate(startDate);
                compFSP.setEndDate(endDate);
                compFSP.setNdays(nDay);
                compFS.setDepartureDate(temp.getArrivalDate());
                compFS.setDepartureTime(temp.getArrivalTime());
                compFS.setEstimatedFlightDuration(flightDuration);
                compFS.calculateAndSetArrivalDateTime();


                 compfspid = flightSchedulePlanSessionBean.createCompliMentaryFlightSchedulePlan(compf, compFSP, compFS, newfspid);
                 compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

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
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newFS.getDepartureDate());
                calendar.add(Calendar.DAY_OF_MONTH, nDay); // Increment by 7 days
                newFS.setDepartureDate(calendar.getTime());
                newFS.calculateAndSetArrivalDateTime(); 
                newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);



                if(hasComFlight)
                {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(newFS.getDepartureDate());
                    calendar2.add(Calendar.DAY_OF_MONTH, nDay); // Increment by 7 days
                    compFS.setDepartureDate(calendar.getTime());
                    compFS.calculateAndSetArrivalDateTime(); 
                    compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

                }
            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(2900), CabinClassTypeEnum.F);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(1400), CabinClassTypeEnum.J);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(400), CabinClassTypeEnum.Y);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            
        } catch (OverlappingScheduleException ex)
        {
            System.out.println(ex.getMessage());
        } catch (ParseException ex)
        {
            ex.printStackTrace();
        }catch (InputDataValidationException ex)
        {
            System.out.println(ex.getMessage());
        } catch (FlightDisabledException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void initFlightSchedulePlan6() {
        FlightSchedulePlan newFSP = new FlightSchedulePlan();
        FlightSchedule newFS = new FlightSchedule();
        FlightSchedulePlan compFSP = new FlightSchedulePlan();
        FlightSchedule compFS = new FlightSchedule();
         
        boolean overlap = false;
        Flight f = new Flight();
        String flightnumber = "";
        
        
        try
        {
           f = flightSessionBean.retrieveFlightByFlightNumber("ML511");
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
               compf = flightSessionBean.retrieveFlightByFlightNumber(f.getComplimentaryFlight().getFlightNumber());
            } catch (FlightNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        List<FlightSchedulePlan> currfsps = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightID(f.getFlightId());
        List<FlightSchedule> currfs = new ArrayList();
        List<FlightSchedule> ongoingfs = new ArrayList();
        for(FlightSchedulePlan fsp: currfsps)
        {
            currfs = flightScheduleSessionBean.retrieveAllFlightSchedulesWithFSPid(fsp.getFlightscheduleplanid());
            for(FlightSchedule fs : currfs)
            {
                ongoingfs.add(fs);
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH 'Hours' mm 'Minute'");
        SimpleDateFormat departureFormat = new SimpleDateFormat("hh:mm");
        
        newFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);

        try {
            Date departureDate;
            Date departureTime;
            Date flightDuration;
            Date layoverDuration = new Date();

            // Prompt and create the first flight schedule
            String departuredatestr = "07 Dec 23";
            String departureTimestr = "5:00 PM";
            String layoverDurationStr = "";
            String flightDurationStr = "3 Hours 00 Minutes";
            if(hasComFlight)
            {
                layoverDurationStr = "2 Hours 00 Minutes";
            }

            Long compfspid = Long.MAX_VALUE; 
            Long compfsid = Long.MAX_VALUE;

            departureDate = dateFormat.parse(departuredatestr);
            departureTime = departureFormat.parse(departureTimestr);
            flightDuration = timeFormat.parse(flightDurationStr);

            newFS.setDepartureDate(departureDate);
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

            Long newfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(f, newFSP, newFS);
            Long newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);

            if(hasComFlight)
            {
                layoverDuration = timeFormat.parse(layoverDurationStr);
                FlightSchedule temp = new FlightSchedule();
                temp.setEstimatedFlightDuration(layoverDuration);
                temp.setDepartureDate(newFS.getArrivalDate());
                temp.setDepartureTime(newFS.getArrivalTime());
                temp.calculateAndSetArrivalDateTime();

                compFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                compFS.setDepartureDate(temp.getArrivalDate());
                compFS.setDepartureTime(temp.getArrivalTime());
                compFS.setEstimatedFlightDuration(flightDuration);
                compFS.calculateAndSetArrivalDateTime();


                compfspid = flightSchedulePlanSessionBean.createCompliMentaryFlightSchedulePlan(compf, compFSP, compFS, newfspid);
                compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

            }

            departuredatestr = "08 Dec 23";
            departureTimestr = "5:00 PM";


            departureDate = dateFormat.parse(departuredatestr);
            departureTime = departureFormat.parse(departureTimestr);
            flightDuration = timeFormat.parse(flightDurationStr);

            newFS.setDepartureDate(departureDate);
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

            newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);

            if(hasComFlight)
            {
                layoverDuration = timeFormat.parse(layoverDurationStr);
                FlightSchedule temp = new FlightSchedule();
                temp.setEstimatedFlightDuration(layoverDuration);
                temp.setDepartureDate(newFS.getArrivalDate());
                temp.setDepartureTime(newFS.getArrivalTime());
                temp.calculateAndSetArrivalDateTime();

                compFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                compFS.setDepartureDate(temp.getArrivalDate());
                compFS.setDepartureTime(temp.getArrivalTime());
                compFS.setEstimatedFlightDuration(flightDuration);
                compFS.calculateAndSetArrivalDateTime();


                //compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

            }

            departuredatestr = "09 Dec 23";
            departureTimestr = "5:00 PM";


            departureDate = dateFormat.parse(departuredatestr);
            departureTime = departureFormat.parse(departureTimestr);
            flightDuration = timeFormat.parse(flightDurationStr);

            newFS.setDepartureDate(departureDate);
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

            newfsid = flightScheduleSessionBean.createNewFlightSchedule(newFS, newfspid);

            if(hasComFlight)
            {
                layoverDuration = timeFormat.parse(layoverDurationStr);
                FlightSchedule temp = new FlightSchedule();
                temp.setEstimatedFlightDuration(layoverDuration);
                temp.setDepartureDate(newFS.getArrivalDate());
                temp.setDepartureTime(newFS.getArrivalTime());
                temp.calculateAndSetArrivalDateTime();

                compFSP.setScheduleType(ScheduleTypeEnum.MULTIPLE);
                compFS.setDepartureDate(temp.getArrivalDate());
                compFS.setDepartureTime(temp.getArrivalTime());
                compFS.setEstimatedFlightDuration(flightDuration);
                compFS.calculateAndSetArrivalDateTime();


                //compfspid = flightSchedulePlanSessionBean.createNewRWFlightSchedulePlan(compf, compFSP, compFS);
                compfsid = flightScheduleSessionBean.createNewFlightSchedule(compFS, compfspid);

            }

            Fare fare = new Fare("F001", BigDecimal.valueOf(3100), CabinClassTypeEnum.F);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("J001", BigDecimal.valueOf(1600), CabinClassTypeEnum.J);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            fare = new Fare("Y001", BigDecimal.valueOf(600), CabinClassTypeEnum.Y);
            fareSessionBeanRemote.createNewFare(fare, newfspid);
            
        } catch (OverlappingScheduleException ex) {
            System.out.println(ex.getMessage());
        } catch (ParseException ex) {
            ex.printStackTrace();

        }catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (FlightDisabledException ex) {
            System.out.println(ex.getMessage());
        }
    }

    
    private void showInputDataValidationErrorsForFlight(Set<ConstraintViolation<Flight>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
     private void showInputDataValidationErrorsForFlightSchedulePlan(Set<ConstraintViolation<FlightSchedulePlan>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
     
       private void showInputDataValidationErrorsForFlightSchedule(Set<ConstraintViolation<FlightSchedule>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
}
