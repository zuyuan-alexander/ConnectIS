/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightReservationSessionBeanRemote;
import entity.CabinClass;
import entity.Customer;
import entity.FlightReservation;
import entity.FlightSchedule;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.CabinClassTypeEnum;
import util.enumeration.TripTypeEnum;
import util.exception.CustomerCredentialExistException;
import util.exception.FlightReservationNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zuyua
 */


public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightReservationSessionBeanRemote flightReservationSessionBeanRemote;
    private FareSessionBeanRemote fareSessionBeanRemote;
    private Customer currentCustomer;
    
    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, FlightReservationSessionBeanRemote flightReservationSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.flightReservationSessionBeanRemote = flightReservationSessionBeanRemote;
        this.fareSessionBeanRemote = fareSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to FRS Mangagement System***\n");
            System.out.println("1: Login");
            System.out.println("2: Register As Customer");
            System.out.println("3: Search Flight");
            System.out.println("4: Exit\n");
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
                        mainMenu();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    doRegisterAsCustomer();
                } else if (response == 3) {
                    doSearchFlight();
                } else if (response == 4)
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
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password); 
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
            System.out.println("Customer with Customer Id " + newCustomerId + " has been successfully created!");
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
            System.out.println("1: Search Flight");
            System.out.println("2: Reserve Flight");
            System.out.println("3: View My Flight Reservation");
            System.out.println("4: View My Flight Reservation Details");
            System.out.println("5: Logout\n");
            
            while(response < 1 || response > 5) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if (response == 1) {
                    doSearchFlight();
                } else if (response == 2) {
                    doReserveFlight();
                } else if (response == 3) {
                    doViewMyFlightReservations();
                } else if (response == 4) {
                    doViewMyFlightReservationDetails();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 5) {
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
        String departureAirport = scanner.nextLine().trim();
        System.out.print("Enter destination airport > ");
        String destinationAirport = scanner.nextLine().trim();
        System.out.print("Enter departure date (dd mm yyyy) > ");
        String dateString = scanner.nextLine().trim();
        
        Date departureDate = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
            departureDate = dateFormat.parse(dateString);
        } catch (ParseException ex) {
            System.out.println("Invalid date format. Please enter the date in dd MM yyyy format.");
        }
        
        Date returnDate = new Date();
        if (tripTypeInt == 2) {
            System.out.print("Enter return date (dd MM yyyy) > ");
            dateString = scanner.nextLine().trim();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
                returnDate = dateFormat.parse(dateString);
            } catch (ParseException ex) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
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
            return;
        }
        
        System.out.print("Do you have any cabin class preferences? (Y: Yes, N: No) > ");
        String cabinClassPreferenceStr = scanner.nextLine().trim();
        Boolean cabinClassPreference = Boolean.FALSE;
        CabinClassTypeEnum cabinClassType = null;
        if (cabinClassPreferenceStr.equalsIgnoreCase("Y")) {
            cabinClassPreference = Boolean.TRUE;
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
            return;
        }
        
        // search direct flight for departure date for ->
        // search connecting flight for departure date for ->
        // search direct flight for departure date for <-
        // search connecting flight for departure date for <-
        
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
                    searchDirectFlight(destinationAirport, departureAirport, departureDate, numOfPassengers, cabinClassType);
                } else if (flightSelect == 2) {
                    // connecting flight
                    searchConnectingFlight(destinationAirport, departureAirport, departureDate, numOfPassengers, cabinClassType);
                }
            } else {
                searchDirectFlight(destinationAirport, departureAirport, departureDate, numOfPassengers, cabinClassType);
                searchConnectingFlight(destinationAirport, departureAirport, departureDate, numOfPassengers, cabinClassType);
            }
        }
    }
    
    public void searchDirectFlight(String origin, String destination, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        // three days before departure date
        for (int i=3; i>=1; i--) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, -i);

            Date newDate = calendar.getTime();
            
            List<FlightSchedule> fsList = flightReservationSessionBeanRemote.searchFlightDirectFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            displayFlightSchedule(fsList, numOfPassengers);
        }
        
        // on the departure date
        List<FlightSchedule> currentFSList = flightReservationSessionBeanRemote.searchFlightDirectFlight(origin, destination, date, numOfPassengers, cabinClassType);
        displayFlightSchedule(currentFSList, numOfPassengers);
        
        // three days after departure date
        for (int i=1; i<=3; i++) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, i);

            Date newDate = calendar.getTime();
            
            List<FlightSchedule> fsList = flightReservationSessionBeanRemote.searchFlightDirectFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            displayFlightSchedule(fsList, numOfPassengers);
        }
    }
    
    public void searchConnectingFlight(String origin, String destination, Date date, Integer numOfPassengers, CabinClassTypeEnum cabinClassType) {
        // three days before departure date
        for (int i=3; i>=1; i--) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, -i);

            Date newDate = calendar.getTime();
            
            List<FlightSchedule> fsList = flightReservationSessionBeanRemote.searchFlightConnectingFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            displayFlightSchedule(fsList, numOfPassengers);
        }
        
        // on the departure date
        List<FlightSchedule> currentFSList = flightReservationSessionBeanRemote.searchFlightConnectingFlight(origin, destination, date, numOfPassengers, cabinClassType);
        displayFlightSchedule(currentFSList, numOfPassengers);
        
        // three days after departure date
        for (int i=1; i<=3; i++) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, i);

            Date newDate = calendar.getTime();
            
            List<FlightSchedule> fsList = flightReservationSessionBeanRemote.searchFlightConnectingFlight(origin, destination, newDate, numOfPassengers, cabinClassType);
            displayFlightSchedule(fsList, numOfPassengers);
        }
    }
    
    public void displayFlightSchedule(List<FlightSchedule> fsList, Integer numOfPassengers) {
        for (FlightSchedule fs : fsList) {
            System.out.println("Flight Schedule Id #" + fs.getFlightscheduleid());
            System.out.println("Flight Number <" + fs.getFlightSchedulePlan().getFlight() + ">");
            System.out.println("Departs at " + fs.getDepartureDate() + " " + fs.getDepartureTime() + "; Arrives at " + fs.getArrivalDate());
            for (CabinClass cabinClass : fs.getFlightSchedulePlan().getFlight().getAircraftConfiguration().getCabinClasses()) {
                BigDecimal fareAmount = fareSessionBeanRemote.retrieveFareAmountByCabinClassType(fs.getFlightSchedulePlan().getFares(), cabinClass);
                System.out.println("Fare Price per Passenger for " + cabinClass.getCabinClassType() + ": " + 
                        fareAmount + "; Total Price for " + numOfPassengers + " Passengers: " + fareAmount.multiply(BigDecimal.valueOf(numOfPassengers)));
            }
            System.out.println();
        }
    }
    
    public void doReserveFlight() {
        System.out.print("Enter flight schedule id > ");
        System.out.print("Enter number of passengers > ");
        
    }
    
    public void doViewMyFlightReservations() {
        System.out.println("===== View My Flight Reservations =====");
        List<FlightReservation> flightReservations = flightReservationSessionBeanRemote.viewMyFlightReservations(currentCustomer);
        System.out.println("For Customer (" + currentCustomer.getUsername() + "), ");
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
            System.out.println("Flight Schedule: ");
            System.out.println("Seat Number: " + "\n");
        } catch (FlightReservationNotFoundException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }
}