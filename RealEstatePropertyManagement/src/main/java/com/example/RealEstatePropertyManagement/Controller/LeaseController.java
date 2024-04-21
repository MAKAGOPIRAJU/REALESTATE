package com.example.RealEstatePropertyManagement.Controller;

import com.example.RealEstatePropertyManagement.Model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.RealEstatePropertyManagement.Model.Lease;
import com.example.RealEstatePropertyManagement.Service.LeaseService;

import java.util.List;

@RestController
public class LeaseController {
    @Autowired
    private LeaseService leaseService;

    @GetMapping("/leases")
    public List<Lease> getLeases() {

        return leaseService.getLeases();
    }

    @GetMapping("/leases/{leaseId}")
    public Lease getLeaseById(@PathVariable("leaseId") int leaseId) {
        return leaseService.getLeaseById(leaseId);
    }

    @PostMapping("/leases/{propertyId}/{ownerId}")
    public Lease addLease(@RequestBody Lease lease ,
                          @PathVariable("propertyId") Integer propertyId,
                          @PathVariable("ownerId") Integer ownerId) {

        return leaseService.addLease(lease , propertyId , ownerId);
    }

    @PutMapping("/leases/{leaseId}")
    public Lease updateLease(@PathVariable("leaseId") int leaseId, @RequestBody Lease lease) {
        return leaseService.updateLease(leaseId, lease);
    }

    @DeleteMapping("/leases/{leaseId}")
    public void deleteLease(@PathVariable("leaseId") int leaseId) {
        leaseService.deleteLease(leaseId);
    }

}
