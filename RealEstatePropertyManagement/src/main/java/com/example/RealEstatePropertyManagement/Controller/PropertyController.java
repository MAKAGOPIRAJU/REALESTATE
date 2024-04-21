package com.example.RealEstatePropertyManagement.Controller;

import com.example.RealEstatePropertyManagement.Model.Lease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.RealEstatePropertyManagement.Model.Property;
import com.example.RealEstatePropertyManagement.Service.PropertyService;

import java.util.List;

@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/properties")
    public List<Property> getProperties(){

        return propertyService.getProperties();
    }

    @GetMapping("/properties/{propertyId}")
    public Property getPropertyById(@PathVariable("propertyId") int propertyId){
        return propertyService.getPropertyById(propertyId);
    }

    @PostMapping("/properties")
    public Property addProperty(@RequestBody Property property){

        return propertyService.addProperty(property);
    }

    @PutMapping("/properties/{propertyId}")
    public Property updateProperty(@RequestBody Property property, @PathVariable("propertyId") int propertyId) {
        return propertyService.updateProperty(propertyId, property);
    }

    @DeleteMapping("/properties/{propertyId}")
    public void deleteProperty(@PathVariable("propertyId") int propertyId) {
        propertyService.deleteProperty(propertyId);
    }

}

