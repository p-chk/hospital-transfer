package dev.chk.HospitalServiceApplication.service;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.model.Patient;
import dev.chk.HospitalServiceApplication.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    List<PatientDto> getAllPatients();

    Patient getPatientById(String id);

    Patient getPatientByPhn(String phn);

    PatientDto getPatientDtoById(String id);

    PatientDto getPatientDtoByPhn(String phn);

    PatientDto addPatient(PatientDto patientDto);
    Patient addPatient(Patient patient);

    PatientDto updatePatient(String id, PatientDto PatientDto);

    Patient updatePatient(Patient PatientDto);

    List<PatientDto> getPatientsByStatus(String status);

}