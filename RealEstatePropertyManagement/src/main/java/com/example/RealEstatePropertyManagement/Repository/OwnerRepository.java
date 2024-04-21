package com.example.RealEstatePropertyManagement.Repository;

import com.example.RealEstatePropertyManagement.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner,Integer> {
}
