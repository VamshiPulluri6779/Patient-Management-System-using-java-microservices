package com.pm.patientservice.Service;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponseDTO;
import com.pm.patientservice.Exception.EmailAlreadyExistsException;
import com.pm.patientservice.Exception.PatientNotFoundException;
import com.pm.patientservice.Model.Patient;
import com.pm.patientservice.Repository.PatientRepository;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    private final KafkaProducer  kafkaProducer;

    // Constructor dependency injection
    PatientService(PatientRepository patientRepository,  BillingServiceGrpcClient billingServiceGrpcClient,
                    KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDTO).collect(Collectors.toList());
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email" +
                    "already exists" + patientRequestDTO.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        // create a billing account for newly created patient
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),
                patient.getName(), patient.getEmail());

        // Put a patient created event into the kafka topic
        kafkaProducer.sendEvent(patient);

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        // First get the patient from the database
        Patient patient = patientRepository.
                findById(id).
                orElseThrow(
                        () -> new PatientNotFoundException("Patient not found with id" + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email" +
                    "already exists" + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
