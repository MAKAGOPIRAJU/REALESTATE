package com.example.RealEstatePropertyManagement.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propertyid;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private String status;

    @Column(name = "price")
    private Integer price;


    @OneToMany(mappedBy = "property")
    @JsonIgnore
    List<Lease> leaseList = new ArrayList<>();

    public Integer getPropertyid() {

        return propertyid;
    }

    public void setPropertyid(Integer propertyid) {

        this.propertyid = propertyid;
    }

    public void setLeaseList(List<Lease> leaseList) {
        this.leaseList = leaseList;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public Integer getPrice() {

        return price;
    }

    public void setPrice(Integer price) {

        this.price = price;
    }


    public List<Lease> getLeases() {

        return leaseList;
    }
}

