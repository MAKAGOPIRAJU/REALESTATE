package com.example.RealEstatePropertyManagement.Service;

import com.example.RealEstatePropertyManagement.Model.Lease;
import com.example.RealEstatePropertyManagement.Model.Owner;
import com.example.RealEstatePropertyManagement.Repository.LeaseRepository;
import com.example.RealEstatePropertyManagement.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.RealEstatePropertyManagement.Model.Property;
import com.example.RealEstatePropertyManagement.Repository.PropertyRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private LeaseRepository leaseRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(int propertyId) {
        Optional<Property> property = propertyRepository.findById(propertyId);
        if (!property.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return property.get();
    }

    public Property addProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property updateProperty(int propertyId, Property propertyDetails) {
        try {
            Property property = propertyRepository.findById(propertyId).get();

             if(propertyDetails.getAddress() != null) property.setAddress(propertyDetails.getAddress());
             if(propertyDetails.getPrice() != null) property.setPrice(propertyDetails.getPrice());
             if(propertyDetails.getStatus() != null) property.setStatus(propertyDetails.getStatus());

            return propertyRepository.save(property);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteProperty(int propertyId) {

        try{

            Property property = propertyRepository.findById(propertyId).get();

            // Get all the Leases have on this property

            List<Lease> leaseList = property.getLeases();

            for(Lease lease : leaseList) {

                Owner owner = lease.getOwner();
                owner.getLeaseList().remove(lease); // remove this lease to that owner
                ownerRepository.save(owner);

                lease.setProperty(null);
                lease.setOwner(null);
            }

             leaseRepository.saveAll(leaseList);

             propertyRepository.delete(property);
        }

        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    public List<Lease> getPropertyLeases(int propertyId) {
        Property property = getPropertyById(propertyId);
        return property.getLeases();
    }
}