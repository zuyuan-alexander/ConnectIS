/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author zuyua
 */
@Entity
public class Flight implements Serializable {

    /**
     * @return the flightscheduleplans
     */
    public List<FlightSchedulePlan> getFlightscheduleplans() {
        return flightscheduleplans;
    }

    /**
     * @param flightscheduleplans the flightscheduleplans to set
     */
    public void setFlightscheduleplans(List<FlightSchedulePlan> flightscheduleplans) {
        this.flightscheduleplans = flightscheduleplans;
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    @Column(nullable=false, unique=true, length = 6)
    @NotNull
    @Size(min = 5, max = 5)
    private String flightNumber;

    @OneToOne(optional=false)
    @JoinColumn(nullable=false)
    private FlightRoute flightRoute;
    
    @ManyToOne(optional=false)
    @JoinColumn(nullable=false)
    private AircraftConfiguration AircraftConfiguration;
    
    @OneToOne(optional=true)
    @JoinColumn(nullable=true)
    private Flight complimentaryFlight;
    
    
    @OneToMany(mappedBy = "flight")
    private List<FlightSchedulePlan> flightscheduleplans;

    public Flight() {
    }

    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * @return the flightId
     */
    public Long getFlightId() {
        return flightId;
    }

    /**
     * @param flightId the flightId to set
     */
    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    /**
     * @return the flightNumber
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * @param flightNumber the flightNumber to set
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * @return the flightRoute
     */
    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    /**
     * @param flightRoute the flightRoute to set
     */
    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }

    /**
     * @return the AircraftConfiguration
     */
    public AircraftConfiguration getAircraftConfiguration() {
        return AircraftConfiguration;
    }

    /**
     * @param AircraftConfiguration the AircraftConfiguration to set
     */
    public void setAircraftConfiguration(AircraftConfiguration AircraftConfiguration) {
        this.AircraftConfiguration = AircraftConfiguration;
    }

    /**
     * @return the complimentaryFlight
     */
    public Flight getComplimentaryFlight() {
        return complimentaryFlight;
    }

    /**
     * @param complimentaryFlight the complimentaryFlight to set
     */
    public void setComplimentaryFlight(Flight complimentaryFlight) {
        this.complimentaryFlight = complimentaryFlight;
    }

   



    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getFlightId() != null ? this.getFlightId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.getFlightId() == null && other.getFlightId() != null) || (this.getFlightId() != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ flightId=" + this.getFlightId() + " ]";
    }

  
}
