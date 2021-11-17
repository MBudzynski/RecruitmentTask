package com.example.springbootclient.physician;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PhysicianRepository extends JpaRepository<Physician,Long> {


}
