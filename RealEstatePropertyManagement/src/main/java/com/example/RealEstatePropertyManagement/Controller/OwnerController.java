package com.example.RealEstatePropertyManagement.Controller;

import com.example.RealEstatePropertyManagement.Model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.RealEstatePropertyManagement.Model.Owner;
import com.example.RealEstatePropertyManagement.Service.OwnerService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/owners")
    public List<Owner> getOwners(){
        return ownerService.getOwners();
    }

    @GetMapping("/owners/{ownerId}")
    public Owner getOwnerById(@PathVariable("ownerId") int ownerId){
        return ownerService.getOwnerById(ownerId);
    }

    @PostMapping("/owners")
    public Owner addOwner(@RequestBody Owner owner){
        return ownerService.addOwner(owner);
    }

    @PutMapping("/owners/{ownerId}")
    public Owner updateOwner(@RequestBody Owner owner, @PathVariable("ownerId") int ownerId) {
        return ownerService.updateOwner(ownerId, owner);
    }

    @DeleteMapping("/owners/{ownerId}")
    public void deleteOwner(@PathVariable("ownerId") int ownerId) {
        ownerService.deleteOwner(ownerId);
    }
}
