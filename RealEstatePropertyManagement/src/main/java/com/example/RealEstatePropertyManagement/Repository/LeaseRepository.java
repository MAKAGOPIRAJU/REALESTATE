package com.example.RealEstatePropertyManagement.Repository;

import com.example.RealEstatePropertyManagement.Model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaseRepository extends JpaRepository<Lease,Integer> {
}
