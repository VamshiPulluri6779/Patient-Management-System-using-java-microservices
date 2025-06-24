package com.pm.patientservice.Repository;

import com.pm.patientservice.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByEmail(String email);
}
