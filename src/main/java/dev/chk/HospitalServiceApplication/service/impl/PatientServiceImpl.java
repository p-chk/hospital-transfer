package dev.chk.HospitalServiceApplication.service.impl;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.mapper.PatientMapper;
import dev.chk.HospitalServiceApplication.model.Patient;
import dev.chk.HospitalServiceApplication.repository.PatientRepository;
import dev.chk.HospitalServiceApplication.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    public Patient getPatientById(String id) {
        return patientRepository.findByPatientId(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));
    }

    public Patient getPatientByPhn(String phn) {
        return patientRepository.findByPhn(phn)
                .orElseThrow(() -> new NotFoundException("Patient not found"));
    }

    @Override
    public PatientDto getPatientDtoById(String id) {
        return patientMapper.toDto(getPatientById(id));
    }

    @Override
    public PatientDto getPatientDtoByPhn(String phn) {
        return patientMapper.toDto(getPatientByPhn(phn));
    }

    public PatientDto addPatient(PatientDto patientDto) {
        Patient newPatient = patientMapper.toEntity(patientDto);
        return patientMapper.toDto(addPatient(newPatient));
    }

    @Override
    public Patient addPatient(Patient patient) {
        String newPatientId = generateNewPatientId();
        patient.setPatientId(newPatientId);

        return patientRepository.save(patient);
    }

    public PatientDto updatePatient(String id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        patientMapper.toEntity(patientDto);
        return patientMapper.toDto(updatePatient(patient));
    }

    @Override
    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    private String generateNewPatientId() {
        long count = patientRepository.count();
        return "AZ" + String.format("%04d", count + 1);
    }

    public List<PatientDto> getPatientsByStatus(String status) {
        return patientRepository.findByStatus(status)
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }
}
