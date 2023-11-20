/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package frsreservationclient;

import ejb.session.stateless.CabinClassSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import ejb.session.stateless.SeatSessionBeanRemote;
import entity.CabinClass;
import entity.Customer;
import entity.Fare;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Passenger;
import entity.Seat;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.TripTypeEnum;
import util.exception.CabinClassNotFoundException;
import util.exception.CustomerCredentialExistException;
import util.exception.CustomerNotFoundException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PassengerAlreadyExistsException;
import util.exception.PassengerNotFoundException;
import util.exception.SeatNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zuyua
 */


public class MainApp {
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private CabinClassSessionBeanRemote cabinClassSessionBeanRemote;
    private SeatSessionBeanRemote seatSessionBeanRemote;
    private PassengerSessionBeanRemote passengerSessionBeanRemote;
    private Customer currentCustomer;
    
    
    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, CabinClassSessionBeanRemote cabinClassSessionBeanRemote, SeatSessionBeanRemote seatSessionBeanRemote) {
        this();
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.cabinClassSessionBeanRemote = cabinClassSessionBeanRemote;
        this.seatSessionBeanRemote = seatSessionBeanRemote;
    }
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Reservation System***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 2)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        mainMenu();
                        
                      
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 1)
                {
                    doRegisterAsCustomer();
                }
                else if (response == 3)
                {
                    doSearchFlight();
              
                } else if (response == 3)
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
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** FRS Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            this.currentCustomer = customerSessionBeanRemote.customerLogin(username, password); 
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    public void doRegisterAsCustomer() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Register As Customer =====");
        System.out.print("Enter first name > ");
        String firstName = sc.nextLine().trim();
        System.out.print("Enter last name > ");
        String lastName = sc.nextLine().trim();
        System.out.print("Enter email > ");
        String email = sc.nextLine().trim();
        System.out.print("Enter mobile phone number > ");
        String mobilePhoneNumber = sc.nextLine().trim();
        System.out.print("Enter address > ");
        String address = sc.nextLine().trim();
        System.out.print("Enter postalCode > ");
        String postalCode = sc.nextLine().trim();
        System.out.print("Enter username > ");
        String username = sc.nextLine().trim();
        System.out.print("Enter password > ");
        String password = sc.nextLine().trim();
        
        Customer newCustomer = new Customer(firstName, lastName, email, mobilePhoneNumber, address, postalCode, username, password);
        try {
            Long newCustomerId = customerSessionBeanRemote.registerAsCustomer(newCustomer);
            System.out.println("Customer " + username + " has been successfully created!");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (CustomerCredentialExistException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Main Menu ***");
            System.out.println("1: Search Flight (Reserve Flight)");
            //System.out.println("2: Reserve Flight");
            System.out.println("2: View My Flight Reservation");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if (response == 1) {
                    doSearchFlight();
                } else if (response == 2) {
                    doViewMyFlightReservations();
                } else if (response == 3) {
                    doViewMyFlightReservationDetails();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 4) {
                break;
            }
        }
    }
    
    public void doSearchFlight() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===== Search Flight =====");
        System.out.print("Enter trip type (1: One-way, 2: Round-trip) > ");
        Integer tripTypeInt = scanner.nextInt();
        scanner.nextLine();
        
        while (tripTypeInt < 1 || tripTypeInt > 2) {
            System.out.println("Invalid input. Please try again!");
            System.out.print("Enter trip type (1: One-way, 2: Round-trip) > ");
            tripTypeInt = scanner.nextInt();
            scanner.nextLine();
        }
        
        TripTypeEnum tripType;
        if (tripTypeInt == 1) {
            tripType = TripTypeEnum.ONE_WAY;
        } else if (tripTypeInt == 2) {
            tripType = TripTypeEnum.ROUND_TRIP;
        }
        
        System.out.print("Enter departure airport > ");
        String departureAirport = scanner.nextLine();
        System.out.print("Enter destination airport > ");
        String destinationAirport = scanner.nextLine().trim();
        System.out.print("Enter departure date (dd MMM yy) > ");
        String dateString = scanner.nextLine().trim();
        
        Date departureDate = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
            departureDate = dateFormat.parse(dateString);
        } catch (ParseException ex) {
            System.out.println("Invalid date format. Please enter the date in dd MMM yy format.");
        }
        
        Date returnDate = new Date();
        if (tripTypeInt == 2) {
            System.out.print("Enter return date (dd MMM yy) > ");
            dateString = scanner.nextLine().trim();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy");
                returnDate = dateFormat.parse(dateString);
            } catch (ParseException ex) {
                System.out.println("Invalid date format. Please enter the date in dd MMM yy format.");
            }
        }
        
        System.out.print("Enter number of passengers > ");
        Integer numOfPassengers = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Do you have any flight preferences? (Y: Yes, N: No) > ");
        String flightPreferenceStr = scanner.nextLine().trim();
        Boolean flightPreference = Boolean.FALSE;
        Integer flightSelect = 0;
        if (flightPreferenceStr.equalsIgnoreCase("Y")) {
            flightPreference = Boolean.TRUE;
            System.out.print("Enter flight preference (1: Direct Flight, 2: Connecting Flight) > ");
            flightSelect = scanner.nextInt();
            scanner.nextLine();
        } else if (flightPreferenceStr.equalsIgnoreCase("N")) {
            flightPreference = Boolean.FALSE;
        } else {
            System.out.println("Invalid Input!");
            
        }
        
        System.out.print("Do you have any cabin class preferences? (Y: Yes, N: No) > ");
        
        String cabinClassPreferenceStr = scanner.nextLine().trim();
        Boolean cabinClassPreference = Boolean.FALSE;
        CabinClassTypeEnum cabinClassType = null;
        if (cabinClassPreferenceStr.equalsIgnoreCase("Y")) {
            cabinClassPreference = Boolean.TRUE;
            System.out.println();
            System.out.print("Enter cabin class type (F: First, J: Business, W: Premium Economy, Y: Economy) > ");
            String ccTypeStr = scanner.nextLine().trim();
            
            if (ccTypeStr.equalsIgnoreCase("F")) {
                cabinClassType = CabinClassTypeEnum.F;
            } else if (ccTypeStr.equalsIgnoreCase("J")) {
                cabinClassType = CabinClassTypeEnum.J;
            } else if (ccTypeStr.equalsIgnoreCase("W")) {
                cabinClassType = CabinClassTypeEnum.W;
            } else if (ccTypeStr.equalsIgnoreCase("Y")) {
                cabinClassType = CabinClassTypeEnum.Y;
            }
            
        } else if (cabinClassPreferenceStr.equalsIgnoreCase("N")) {
            cabinClassPreference = Boolean.FALSE;
        } else {
            System.out.println("Invalid Input!");
            
    
        }
        
        // one way
        System.out.println("Flights from " + departureAirport + " to " + destinationAirport);
        
        if (flightPreference) {
            if (flightSelect == 1) {
                // direct flight
                searchDirectFlight(departureAirport, destinationAirport, departureDate, numOfPassengers, cabinClassType);
            } else if (flightSelect == 2) {
                // connecting flight
                searchConnectingFlight(departureAirport, destinationAirport, departureDate, numOfPassengers, cabinClassType);
            }
        } else {
            searchDirectFlight(departureAirport, destinationAirport, departureDate, numOfPassengers, cabinClassType);
            searchConnectingFlight(departureAirport, destinationAirport, departureDate, numOfPassengers, cabinClassType);
        }
        System.out.println();
        
        // round trip
        if (tripTypeInt == 2) {
            System.out.println("Return Flights from " + destinationAirport + " to " + departureAirport);
            if (flightPreference) {
                if (flightSelect == 1) {
                    // direct flight
                    searchDirectFlight(destinationAirport, departureAirport, returnDate, numOfPassengers, cabinClassType);
                } else if (flightSelect == 2) {
                    // connecting flight
                    searchConnectingFlight(destinationAirport, departureAirport, returnDate, numOfPassengers, cabinClassType);
                }
            } else {
                searchDirectFlight(destinationAirport, departureAirport, returnDate, numOfPassengers, cabinClassType);
                searchConnectingFlight(destinationAirport, departureAirport, returnDate, numOfPassengers, cabinClassType);
            }
        }
        
        if(currentCustomer != null)
        {
            System.out.print("Do you want to reserve a Flight: Y/N > ");
            String response = scanner.nextLine();
            if(response.equals("Y"))
            {
                //System.out.print("Enter flight schedule id to reserve > ");
                //Long mainfsid = scanner.nextLong();
                //scanner.nextLine();
                // flightSelect = 0;
                // cabinClassType = null;
                doReserveFlight(tripTypeInt, flightSelect, cabinClassType, numOfPassengers);
            }
        }
        
        
    }
    
    public void searchDirectFlight(String origin, String destination, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        // three days before departure date
        for (int i=3; i>=1; i--) {
            System.out.println("*** On Departure Date -" + i + " ***");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, -i);

            Date newDate = calendar.getTime();
            
            List<FlightSchedule> fsList = flightReservationSessionBeanRemote.searchFlightDirectFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            if(!fsList.isEmpty())
            {
                displayFlightSchedule(fsList, numOfPassengers);
            }
            
        }
        
        // on the departure date
        System.out.println("*** On Departure Date ***");
        List<FlightSchedule> currentFSList = flightReservationSessionBeanRemote.searchFlightDirectFlight(origin, destination, date, numOfPassengers, cabinClassType);
        displayFlightSchedule(currentFSList, numOfPassengers);
        
        // three days after departure date
        for (int i=1; i<=3; i++) {
            System.out.println("*** On Departure Date +" + i + " ***");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, i);

            Date newDate = calendar.getTime();
            
            List<FlightSchedule> fsList = flightReservationSessionBeanRemote.searchFlightDirectFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
             if(!fsList.isEmpty())
            {
                displayFlightSchedule(fsList, numOfPassengers);
            }
        }
    }
    
    public void searchConnectingFlight(String origin, String destination, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        // three days before departure date
        for (int i=3; i>=1; i--) {
            System.out.println("*** On Departure Date -" + i + " ***");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, -i);

            Date newDate = calendar.getTime();
            
            //List<FlightSchedule> fsFirstList = flightReservationSessionBeanRemote.searchFlightConnectingFlightFirst(origin, destination, newDate, numOfPassengers, cabinClassType);
            //List<FlightSchedule> fsSecondList = flightReservationSessionBeanRemote.searchFlightConnectingFlightSecond(origin, destination, newDate, numOfPassengers, cabinClassType);
            List<Object[]> fsFirstList = flightReservationSessionBeanRemote.searchFlightConnectingFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            if(!fsFirstList.isEmpty())
            {
                //displayFlightSchedule(fsFirstList, numOfPassengers);
                //displayFlightSchedule(fsSecondList, numOfPassengers);
                displayConnectingFlightSchedule(fsFirstList, numOfPassengers);
            }
        }
        
        // on the departure date
        System.out.println("*** On Departure Date ***");
        List<Object[]> currentFSFirstList = flightReservationSessionBeanRemote.searchFlightConnectingFlight(origin, destination, date, numOfPassengers, cabinClassType);
        displayConnectingFlightSchedule(currentFSFirstList, numOfPassengers);
        //List<FlightSchedule> currentFSFirstList = flightReservationSessionBeanRemote.searchFlightConnectingFlightFirst(origin, destination, date, numOfPassengers, cabinClassType);
        //List<FlightSchedule> currentFSSecondList = flightReservationSessionBeanRemote.searchFlightConnectingFlightSecond(origin, destination, date, numOfPassengers, cabinClassType);
        //displayFlightSchedule(currentFSFirstList, numOfPassengers);
        //displayFlightSchedule(currentFSSecondList, numOfPassengers);
        
        // three days after departure date
        for (int i=1; i<=3; i++) {
            System.out.println("*** On Departure Date +" + i + " ***");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, i);

            Date newDate = calendar.getTime();
            
            //List<FlightSchedule> fsFirstList = flightReservationSessionBeanRemote.searchFlightConnectingFlightFirst(origin, destination, newDate, numOfPassengers, cabinClassType);
            //List<FlightSchedule> fsSecondList = flightReservationSessionBeanRemote.searchFlightConnectingFlightSecond(origin, destination, newDate, numOfPassengers, cabinClassType);
            List<Object[]> fsFirstList = flightReservationSessionBeanRemote.searchFlightConnectingFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            if(!fsFirstList.isEmpty())
            {
                //displayFlightSchedule(fsFirstList, numOfPassengers);
                //displayFlightSchedule(fsSecondList, numOfPassengers);
                displayConnectingFlightSchedule(fsFirstList, numOfPassengers);
            }
        }
    }
    
    public void displayConnectingFlightSchedule(List<Object[]> fsList, Integer numOfPassengers) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        FlightSchedule currFirstFS = new FlightSchedule();
        FlightSchedule currSecondFS = new FlightSchedule();


        if(fsList.isEmpty())
        {
            System.out.println("No Available Flights");
        }
        for (Object[] obj : fsList) {
            FlightSchedule firstFS = (FlightSchedule) obj[0];
            FlightSchedule secondFS = (FlightSchedule) obj[1];
            try
            {
                currFirstFS = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(firstFS.getFlightscheduleid());
                currSecondFS = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(secondFS.getFlightscheduleid());
            } catch (FlightScheduleNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            } 
            
            System.out.println("+++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Flight Schedule ID #" + currFirstFS.getFlightscheduleid());
            System.out.println("Flight Number <" + currFirstFS.getFlightSchedulePlan().getFlight().getFlightNumber() + ">");
            System.out.println("Flight from " + currFirstFS.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin() + " -> " + currFirstFS.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination());
            System.out.println("Departs at " + dateFormat.format(currFirstFS.getDepartureDate()) + " " +
                   timeFormat.format(currFirstFS.getDepartureTime()) + 
                   "; Arrives at " + dateFormat.format(currFirstFS.getArrivalDate()) +
                   " " + timeFormat.format(currFirstFS.getArrivalTime()));
            for (Fare fare  : currFirstFS.getFlightSchedulePlan().getFares()) {
                BigDecimal fareamount = BigDecimal.ZERO;
                fareamount = fare.getFareAmount();
                String cabinclasstype = fare.getCabinClassType().toString();
                  System.out.println("Fare Price per Passenger for " + cabinclasstype + ": " + 
                        fareamount + "; Total Price for " + numOfPassengers + " Passengers: " + fareamount.multiply(BigDecimal.valueOf(numOfPassengers)));
            }
            System.out.println();
            
            System.out.println("Flight Schedule ID #" + currSecondFS.getFlightscheduleid());
            System.out.println("Flight Number <" + currSecondFS.getFlightSchedulePlan().getFlight().getFlightNumber() + ">");
            System.out.println("Flight from " + currSecondFS.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin() + " -> " + currSecondFS.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination());
            System.out.println("Departs at " + dateFormat.format(currSecondFS.getDepartureDate()) + " " +
                   timeFormat.format(currSecondFS.getDepartureTime()) + 
                   "; Arrives at " + dateFormat.format(currSecondFS.getArrivalDate()) +
                   " " + timeFormat.format(currFirstFS.getArrivalTime()));
            for (Fare fare  : currSecondFS.getFlightSchedulePlan().getFares()) {
                BigDecimal fareamount = BigDecimal.ZERO;
                fareamount = fare.getFareAmount();
                String cabinclasstype = fare.getCabinClassType().toString();
                  System.out.println("Fare Price per Passenger for " + cabinclasstype + ": " + 
                        fareamount + "; Total Price for " + numOfPassengers + " Passengers: " + fareamount.multiply(BigDecimal.valueOf(numOfPassengers)));
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++\n\n");
        }
    }
    
    public void displayFlightSchedule(List<FlightSchedule> fsList, Integer numOfPassengers) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        FlightSchedule currfs = new FlightSchedule();


        if(fsList.isEmpty())
        {
            System.out.println("No Available Flights");
        }
        for (FlightSchedule fs : fsList) {
            try
            {
                currfs = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(fs.getFlightscheduleid());
            } catch (FlightScheduleNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            } 
            
            System.out.println("Flight Schedule ID #" + currfs.getFlightscheduleid());
            System.out.println("Flight Number <" + currfs.getFlightSchedulePlan().getFlight().getFlightNumber() + ">");
            System.out.println("Flight from " + currfs.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin() + " -> " + currfs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination());
            System.out.println("Departs at " + dateFormat.format(currfs.getDepartureDate()) + " " +
                   timeFormat.format(currfs.getDepartureTime()) + 
                   "; Arrives at " + dateFormat.format(currfs.getArrivalDate()) +
                   " " + timeFormat.format(currfs.getArrivalTime()));
            for (Fare fare  : currfs.getFlightSchedulePlan().getFares()) {
                BigDecimal fareamount = BigDecimal.ZERO;
                fareamount = fare.getFareAmount();
                String cabinclasstype = fare.getCabinClassType().toString();
                  System.out.println("Fare Price per Passenger for " + cabinclasstype + ": " + 
                        fareamount + "; Total Price for " + numOfPassengers + " Passengers: " + fareamount.multiply(BigDecimal.valueOf(numOfPassengers)));
                
                /*
                BigDecimal fareAmount = fareSessionBeanRemote.retrieveFareAmountByCabinClassType(fs.getFlightSchedulePlan().getFares(), cabinClass);
                System.out.println("Fare Price per Passenger for " + cabinClass.getCabinClassType() + ": " + 
                        fareAmount + "; Total Price for " + numOfPassengers + " Passengers: " + fareAmount.multiply(BigDecimal.valueOf(numOfPassengers))); */
            }
            System.out.println();
        }
    }
    
    public void doReserveFlight(Integer tripTypeInt, Integer flightPreferenceInt, CabinClassTypeEnum cabinClassTypeEnum, Integer numOfPassengers) {
        System.out.println("===== Reserve Flight =====");
        Scanner sc = new Scanner(System.in);
        TripTypeEnum tripTpe = TripTypeEnum.ONE_WAY;
        
        // one way
        //Long fsId = null;
        Long mainfsid = null;
        Long returnfsid = null;
        Long mainFirstFSId = null;
        Long mainSecondFSId = null;
        Long returnFirstFSId = null;
        Long returnSecondFSId = null;
        if (flightPreferenceInt == 0) {
            System.out.print("Enter flight preference for reservation (1: Direct, 2: Connecting) > ");
            flightPreferenceInt = sc.nextInt();
            sc.nextLine();
        }
        
        if (flightPreferenceInt == 1) {
            System.out.print("Enter flight schedule id to reserve> ");
            mainfsid = sc.nextLong();
            sc.nextLine();
        } else if (flightPreferenceInt == 2) {
            System.out.print("Enter first flight schedule id to reserve> ");
            mainFirstFSId = sc.nextLong();
            sc.nextLine();
            System.out.print("Enter second flight schedule id to reserve> ");
            mainSecondFSId = sc.nextLong();
            sc.nextLine();
        }
        
        // round trip
        if (tripTypeInt == 2) {
            tripTpe = TripTypeEnum.ROUND_TRIP;
            if (flightPreferenceInt == 0 || flightPreferenceInt == 1) {
                System.out.print("Enter return flight schedule id to reserve> ");
                returnfsid = sc.nextLong();
                sc.nextLine();
            } else if (flightPreferenceInt == 2) {
                System.out.print("Enter first return flight schedule id to reserve> ");
                returnFirstFSId = sc.nextLong();
                sc.nextLine();
                System.out.print("Enter second return flight schedule id to reserve> ");
                returnSecondFSId = sc.nextLong();
                sc.nextLine();
            }
        }
        
        FlightSchedule mainfs = new FlightSchedule();
        FlightSchedule returnfs = new FlightSchedule();
        FlightSchedule mainFirstFS = new FlightSchedule();
        FlightSchedule mainSecondFS = new FlightSchedule();
        FlightSchedule returnFirstFS = new FlightSchedule();
        FlightSchedule returnSecondFS = new FlightSchedule();
        CabinClassTypeEnum cabinClassType = cabinClassTypeEnum; 
        if (cabinClassType == null) {
            System.out.print("Enter cabin class type (F: First, J: Business, W: Premium Economy, Y: Economy) > ");
            String ccTypeStr = sc.nextLine().trim();
            
            if (ccTypeStr.equalsIgnoreCase("F")) {
                cabinClassType = CabinClassTypeEnum.F;
            } else if (ccTypeStr.equalsIgnoreCase("J")) {
                cabinClassType = CabinClassTypeEnum.J;
            } else if (ccTypeStr.equalsIgnoreCase("W")) {
                cabinClassType = CabinClassTypeEnum.W;
            } else if (ccTypeStr.equalsIgnoreCase("Y")) {
                cabinClassType = CabinClassTypeEnum.Y;
            }
        }
        
        
        //Print out the seat configuration.s
        CabinClass preferedCabinClass = new CabinClass();
        try 
        {
            if (flightPreferenceInt == 1) {
                // direct flight
                mainfs = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(mainfsid);
                mainfsid = mainfs.getFlightscheduleid();

                //Flight f = flight
                //List<CabinClass> cabinclassavail = fs.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                //printSeatViewForCabinClasses(cabinclassavail);

                // display this cabin class only
                System.out.println("*** Departure Flight Schedule ***");
                System.out.println("Flight Number: " + mainfs.getFlightSchedulePlan().getFlight().getFlightNumber());
                List<CabinClass> cabinclassavail = mainfs.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                preferedCabinClass = cabinClassSessionBeanRemote.retrievePreferedCabinClassType(cabinclassavail, cabinClassType);
                List<CabinClass> temp = new ArrayList<>();
                temp.add(preferedCabinClass);
                printSeatViewForCabinClasses(temp);

                if (tripTypeInt == 2) {
                    System.out.println("*** Return Flight Schedules ***");
                    returnfs = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(returnfsid);
                    returnfsid = returnfs.getFlightscheduleid();
                    System.out.println("Flight Number: " + returnfs.getFlightSchedulePlan().getFlight().getFlightNumber());
                    cabinclassavail = returnfs.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                    preferedCabinClass = cabinClassSessionBeanRemote.retrievePreferedCabinClassType(cabinclassavail, cabinClassType);
                    temp = new ArrayList<>();
                    temp.add(preferedCabinClass);
                    printSeatViewForCabinClasses(temp);
                }
            } else if (flightPreferenceInt == 2) {
                // connecting flight
                mainFirstFS = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(mainFirstFSId);
                mainFirstFSId = mainFirstFS.getFlightscheduleid();

                //Flight f = flight
                //List<CabinClass> cabinclassavail = fs.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                //printSeatViewForCabinClasses(cabinclassavail);

                // display this cabin class only
                System.out.println("*** Departure Flight Schedule ***");
                System.out.println("Flight Number: " + mainFirstFS.getFlightSchedulePlan().getFlight().getFlightNumber());
                List<CabinClass> cabinclassavail = mainFirstFS.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                preferedCabinClass = cabinClassSessionBeanRemote.retrievePreferedCabinClassType(cabinclassavail, cabinClassType);
                List<CabinClass> temp = new ArrayList<>();
                temp.add(preferedCabinClass);
                printSeatViewForCabinClasses(temp);
                System.out.println("\n");
                
                mainSecondFS = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(mainSecondFSId);
                mainSecondFSId = mainSecondFS.getFlightscheduleid();
                System.out.println("Flight Number: " + mainSecondFS.getFlightSchedulePlan().getFlight().getFlightNumber());
                cabinclassavail = mainSecondFS.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                preferedCabinClass = cabinClassSessionBeanRemote.retrievePreferedCabinClassType(cabinclassavail, cabinClassType);
                temp = new ArrayList<>();
                temp.add(preferedCabinClass);
                printSeatViewForCabinClasses(temp);

                if (tripTypeInt == 2) {
                    System.out.println("*** Return Flight Schedules ***");
                    returnFirstFS = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(returnFirstFSId);
                    returnFirstFSId = returnFirstFS.getFlightscheduleid();
                    System.out.println("Flight Number: " + returnFirstFS.getFlightSchedulePlan().getFlight().getFlightNumber());
                    cabinclassavail = returnFirstFS.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                    preferedCabinClass = cabinClassSessionBeanRemote.retrievePreferedCabinClassType(cabinclassavail, cabinClassType);
                    temp = new ArrayList<>();
                    temp.add(preferedCabinClass);
                    printSeatViewForCabinClasses(temp);
                    System.out.println("\n");
                    
                    returnSecondFS = flightScheduleSessionBeanRemote.retrieveFlightScheduleById(returnSecondFSId);
                    returnSecondFSId = returnSecondFS.getFlightscheduleid();
                    System.out.println("Flight Number: " + returnSecondFS.getFlightSchedulePlan().getFlight().getFlightNumber());
                    cabinclassavail = returnSecondFS.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses();
                    preferedCabinClass = cabinClassSessionBeanRemote.retrievePreferedCabinClassType(cabinclassavail, cabinClassType);
                    temp = new ArrayList<>();
                    temp.add(preferedCabinClass);
                    printSeatViewForCabinClasses(temp);
                    
                }
            }
        } catch (FlightScheduleNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
        
        if(mainfsid == null)
        {
            System.out.println("mainfsid is null");
        }
        
        for (int i=1; i<=numOfPassengers; i++) {
            TripTypeEnum tripTemp = tripTpe;
            CabinClass cabinClassTemp = preferedCabinClass;
            
            if (tripTypeInt == 1) {
                System.out.println("---Departure Flight Reservation for Passenger " + i + "---");
                reserveFlightPassengerInput(cabinClassTemp, mainfs, mainfsid, tripTemp);
                System.out.println("\n"); 

                if (tripTypeInt == 2) {
                    System.out.println("---Return Flight Reservation for Passenger " + i + "---");
                    reserveFlightPassengerInput(cabinClassTemp, returnfs, returnfsid, tripTemp);
                }
                System.out.println("\n\n");
                
            } else if (tripTypeInt == 2) {
                System.out.println("---Departure Flight Reservation for Passenger " + i + "---");
                reserveFlightPassengerInput(cabinClassTemp, mainFirstFS, mainFirstFSId, tripTemp);
                reserveFlightPassengerInput(cabinClassTemp, mainSecondFS, mainSecondFSId, tripTemp);
                System.out.println("\n"); 

                if (tripTypeInt == 2) {
                    System.out.println("---Return Flight Reservation for Passenger " + i + "---");
                    reserveFlightPassengerInput(cabinClassTemp, returnFirstFS, returnFirstFSId, tripTemp);
                    reserveFlightPassengerInput(cabinClassTemp, returnSecondFS, returnSecondFSId, tripTemp);
                }
                System.out.println("\n\n");
            }
        }
    }
    
    public void reserveFlightPassengerInput(CabinClass preferedCabinClass, FlightSchedule flightSchedule, Long flightScheduleId, TripTypeEnum tripType) {
                Scanner sc = new Scanner(System.in);
        System.out.print("Enter first name > ");
        String firstName = sc.nextLine().trim();
        System.out.print("Enter last name > ");
        String lastName = sc.nextLine().trim();
        System.out.print("Enter passport number > ");
        String passportNumber = sc.nextLine().trim();
        System.out.print("Enter prefered seat letter > ");
        Character seatLetter = sc.nextLine().charAt(0);
        System.out.print("Enter prefered seat row number > ");
        Integer seatRowNumber = sc.nextInt();
        sc.nextLine();
        Passenger newPassenger = new Passenger(firstName, lastName, passportNumber);
        //find the seat in the db.
        Seat chosenseat = new Seat();
        try
        {
            chosenseat = seatSessionBeanRemote.retrieveSeatBySeatLetterAndRowNumber(seatLetter, seatRowNumber, preferedCabinClass.getCabinClassId());
        } catch (SeatNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        //i want to check that this passenger has not already booked this fs. i.e 1 seat per passenger
        List<Passenger> passengerlist = flightSchedule.getPassengers();
        boolean booked = false;
        for(Passenger p: passengerlist)
        {
            if(p.getPassportNumber() == passportNumber)
            {
                booked = true;
                break;
            }
        }

        Long passengerid;
        Long flightreservationid;
        if(!booked)
        {
            try
            {
                //FlightReservation flightReservation
                //passengerid = passengerSessionBeanRemote.createNewPassenger(newPassenger, chosenseat.getSeatId(), fs.getFlightscheduleid());
                //flightreservationid = flightReservationSessionBeanRemote.reserveFlightMain(this.currentCustomer.getCustomerid(), fsId, newPassenger, chosenseat.getSeatId(), tripTpe);
                flightreservationid = flightReservationSessionBeanRemote.createNewFlightReservation(this.currentCustomer.getCustomerid(), flightScheduleId, newPassenger, chosenseat.getSeatId(), tripType);
            } 
            /*catch (PassengerAlreadyExistsException | UnknownPersistenceException | InputDataValidationException ex)
            {
                System.out.println(ex.getMessage());
            } */
            catch (CustomerNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (FlightScheduleNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (SeatNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }
    
    public void doViewMyFlightReservations() {
        System.out.println("===== View My Flight Reservations =====");
        List<FlightReservation> flightReservations = flightReservationSessionBeanRemote.viewMyFlightReservations(this.currentCustomer.getCustomerid());
        System.out.println("For Customer (" + this.currentCustomer.getUsername() + "), ");
        for (FlightReservation fr : flightReservations) {
            System.out.println("Flight Reservation Id: " + fr.getFlightreservationid() + "; Trip Type: " + fr.getTripTypeEnum());
        }
    }
    
    public void doViewMyFlightReservationDetails() {
        System.out.println("===== View My Flight Reservation Details =====");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter flight reservation id > ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        try {
            FlightReservation fr = flightReservationSessionBeanRemote.retrieveFlightReservationById(id);
            System.out.println("Flight Reservation Id: " + id);
            System.out.println("Customer: " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
            System.out.println("Passenger: " + fr.getPassenger().getFirstName() + " " + fr.getPassenger().getLastName());
            System.out.println("Flight Schedule: " + fr.getFlightSchedules());
            System.out.println("Seat Number: " + fr.getSeat().toString() + "\n");
        } catch (FlightReservationNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    
    public void printSeatViewForCabinClasses(List<CabinClass> cabinClasses) {
        CabinClass currcc = new CabinClass();
        List<FlightReservation> frList = this.currentCustomer.getFlightReservations();
        
        
        for (CabinClass cabinClass : cabinClasses) {
            try
            {
                 currcc = cabinClassSessionBeanRemote.retrieveCabinClassByID(cabinClass.getCabinClassId());
            } catch (CabinClassNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }

            System.out.println("Cabin Class: " + currcc.getCabinClassType());
            List<Seat> seats = currcc.getSeats();

            System.out.println("Total seat size : " + seats.size());
            List<Seat> reservedSeats = seatSessionBeanRemote.retrieveAllReservedSeats(cabinClass.getCabinClassId());

            int seatIndex = 0;
            for (int row = 1; row <= currcc.getNumOfRows(); row++) {
                for (int seatAbreast = 0; seatAbreast < currcc.getNumOfSeatsAbreast(); seatAbreast++) {
                    if (seatIndex < seats.size()) {

                        Seat seat = seats.get(seatIndex);
                        if (reservedSeats.contains(seats.get(seatIndex))) {
                            System.out.print(seat.toString() + "* ");
                        } else {
                            System.out.print(seat.toString() + " ");
                        }
                        seatIndex++;
                    }
                }
                System.out.println();
            } 
            System.out.println();
        }
    }
    
    /*
    public List<Seat> checkSeatReserved(Seat seat, Customer customer) {
        List<FlightReservation> frList = customer.getFlightReservations();
        List<Seat> seats = new ArrayList<>();
        
        for (FlightReservation fr : frList) {
            Seat reservedSeat = fr.getPassenger().getSeat();
            seats.add(reservedSeat);
        }
        
        return seats;
    }*/
}
