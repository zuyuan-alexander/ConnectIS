/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alvintjw
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightscheduleid;
    @Column(nullable = true)
    private Date departureDate;
    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    @NotNull
    private Date departureTime;
     @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    @NotNull
    private Date estimatedFlightDuration;
    @Column(nullable = true)
    private Date arrivalDate;
   

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;

    /*
    @ManyToMany(mappedBy = "flightschedule")
    private List<Passenger> passengers = new ArrayList<Passenger>(); */

    public FlightSchedule() {
    }

    public FlightSchedule(Date departureDate, Date departureTime, Date estimatedFlightDuration) {
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.estimatedFlightDuration = estimatedFlightDuration;
    }
    
    

    
    public Long getFlightscheduleid() {
        return flightscheduleid;
    }

    public void setFlightscheduleid(Long flightscheduleid) {
        this.flightscheduleid = flightscheduleid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightscheduleid != null ? flightscheduleid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightscheduleid fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightscheduleid == null && other.flightscheduleid != null) || (this.flightscheduleid != null && !this.flightscheduleid.equals(other.flightscheduleid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightscheduleid + " ]";
    }

  
    /**
     * @return the departureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return the departureTime
     */
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @return the estimatedFlightDuration
     */
    public Date getEstimatedFlightDuration() {
        return estimatedFlightDuration;
    }

    /**
     * @param estimatedFlightDuration the estimatedFlightDuration to set
     */
    public void setEstimatedFlightDuration(Date estimatedFlightDuration) {
        this.estimatedFlightDuration = estimatedFlightDuration;
    }

    /**
     * @return the arrivalDate
     */
    public Date getArrivalDate() {
        return arrivalDate;
    }

    /**
     * @param arrivalDate the arrivalDate to set
     */
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    /**
     * @return the flightSchedulePlan
     */
    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    /**
     * @param flightSchedulePlan the flightSchedulePlan to set
     */
    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
    
}
