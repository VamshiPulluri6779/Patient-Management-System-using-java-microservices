package com.pm.patientservice.controller;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponseDTO;
import com.pm.patientservice.DTO.validators.CreatePatientValidationGroup;
import com.pm.patientservice.Service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name ="Patient", description = "Api for managing Patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "get patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // @Validated makes sure that validation happens with associated group
    @PostMapping
    @Operation(summary = "create a new patient")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO){
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update an existing patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                @Valid @RequestBody PatientRequestDTO patientRequestDTO){
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete an existing patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}
