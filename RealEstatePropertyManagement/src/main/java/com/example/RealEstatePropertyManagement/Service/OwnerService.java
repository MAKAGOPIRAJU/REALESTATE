package com.example.RealEstatePropertyManagement.Service;

import com.example.RealEstatePropertyManagement.Model.Lease;
import com.example.RealEstatePropertyManagement.Model.Property;
import com.example.RealEstatePropertyManagement.Repository.LeaseRepository;
import com.example.RealEstatePropertyManagement.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.example.RealEstatePropertyManagement.Model.Owner;
import com.example.RealEstatePropertyManagement.Repository.OwnerRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private LeaseRepository leaseRepository;

    @Autowired
    private PropertyRepository propertyRepository;


    public List<Owner> getOwners() {

        return ownerRepository.findAll();
    }

    public Owner getOwnerById(int ownerId) {
        Optional<Owner> owner = ownerRepository.findById(ownerId);
        if (!owner.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return owner.get();
    }

    public Owner addOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    public Owner updateOwner(int ownerId, Owner ownerDetails) {
        Owner owner = getOwnerById(ownerId);
        owner.setName(ownerDetails.getName());
        return ownerRepository.save(owner);
    }

    public void deleteOwner(int ownerId) {

         try{
               Owner owner = ownerRepository.findById(ownerId).get();

               List<Lease> leaseList = owner.getLeaseList();

               for (Lease lease : leaseList) {

                   Property property = lease.getProperty();

                   property.getLeases().remove(lease); // remove this lease on that property

                   propertyRepository.save(property);

                   lease.setOwner(null);
                   lease.setProperty(null);

               }

               leaseRepository.saveAll(leaseList);

               ownerRepository.delete(owner);
         }

         catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
         }

         throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    public List<Property> getOwnerProperties(int ownerId) {

        try {
            Owner owner = ownerRepository.findById(ownerId).get();

            List<Lease> leaseList = owner.getLeaseList();

            List<Property> properties = new ArrayList<>();

            for(Lease lease : leaseList) {

                Property property = lease.getProperty();

                properties.add(property);
            }

            return properties;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        }
    }
}
