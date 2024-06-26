/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author alvintjw
 */
@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerid;
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(min = 1, max = 32)
    private String name;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 1, max = 32)
    private String username;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 8, max = 64)// Adjust the length as needed
    private String password;

    public Partner(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Partner() {
    }

    public Long getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(Long partnerid) {
        this.partnerid = partnerid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerid != null ? partnerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerid fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerid == null && other.partnerid != null) || (this.partnerid != null && !this.partnerid.equals(other.partnerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerid + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
}
