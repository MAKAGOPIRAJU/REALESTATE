package com.example.RealEstatePropertyManagement.Service;

import com.example.RealEstatePropertyManagement.Model.Owner;
import com.example.RealEstatePropertyManagement.Model.Property;
import com.example.RealEstatePropertyManagement.Repository.OwnerRepository;
import com.example.RealEstatePropertyManagement.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.RealEstatePropertyManagement.Model.Lease;
import com.example.RealEstatePropertyManagement.Repository.LeaseRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class LeaseService {

    @Autowired
    private LeaseRepository leaseRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private OwnerRepository ownerRepository;


    public List<Lease> getLeases() {
        return leaseRepository.findAll();
    }

    public Lease getLeaseById(int leaseId) {
        Optional<Lease> lease = leaseRepository.findById(leaseId);

        if (!lease.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return lease.get();
    }

    public Lease addLease(Lease lease , Integer propertyId , Integer ownerId) {

        Property property = propertyRepository.findById(propertyId).get();

        if(!property.getStatus().equals("Available")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        property.setStatus("Not-Available");
        propertyRepository.save(property);

        Owner owner = ownerRepository.findById(ownerId).get();

        lease.setOwner(owner);
        lease.setProperty(property);

        leaseRepository.save(lease);

        return lease;
    }

    public Lease updateLease(int leaseId, Lease leaseDetails) {
        try {
            Lease lease = getLeaseById(leaseId);
            if(leaseDetails.getStartdate() != null) lease.setStartdate(leaseDetails.getStartdate());
            if(leaseDetails.getEnddate() != null) lease.setEnddate(leaseDetails.getEnddate());
            if(leaseDetails.getProperty() != null) lease.setProperty(leaseDetails.getProperty());
            if(leaseDetails.getOwner() != null) lease.setOwner(leaseDetails.getOwner());
            return leaseRepository.save(lease);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLease(int leaseId) {

        try{
            Lease lease = leaseRepository.findById(leaseId).get();

            Owner owner = lease.getOwner();

            owner.getLeaseList().remove(lease);

            ownerRepository.save(owner);

            Property property = lease.getProperty();

            property.getLeases().remove(lease);

            propertyRepository.save(property);

            leaseRepository.delete(lease);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }


}
