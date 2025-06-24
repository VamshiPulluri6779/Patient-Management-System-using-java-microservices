package com.pm.patientservice.mapper;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponseDTO;
import com.pm.patientservice.Model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient p) {
        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setId(p.getId().toString());
        patientDTO.setName(p.getName());
        patientDTO.setEmail(p.getEmail());
        patientDTO.setAddress(p.getAddress());
        patientDTO.setDateOfBirth(p.getDateOfBirth().toString());
        return patientDTO;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }
}
