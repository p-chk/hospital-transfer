package dev.chk.HospitalServiceApplication.service.impl;

import dev.chk.HospitalServiceApplication.dto.OutgoingTransferDto;
import dev.chk.HospitalServiceApplication.mapper.OutgoingTransferMapper;
import dev.chk.HospitalServiceApplication.mapper.PatientMapper;
import dev.chk.HospitalServiceApplication.model.OutgoingTransfer;
import dev.chk.HospitalServiceApplication.model.Patient;
import dev.chk.HospitalServiceApplication.repository.OutgoingTransferRepository;
import dev.chk.HospitalServiceApplication.service.HospitalService;
import dev.chk.HospitalServiceApplication.service.OutgoingTransferService;
import dev.chk.HospitalServiceApplication.service.PatientService;
import dev.chk.HospitalServiceApplication.util.HL7MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import static dev.chk.HospitalServiceApplication.constant.ADTMessageTypeConstants.*;

@Service
@RequiredArgsConstructor
public class OutgoingTransferServiceImpl implements OutgoingTransferService {

    private final OutgoingTransferRepository outgoingTransferRepository;
    private final PatientService patientService;
    private final HospitalService hospitalService;
    private final IguanaXServiceImpl iguanaXService;
    private final OutgoingTransferMapper outgoingTransferMapper;
    private final PatientMapper patientMapper;

    @Override
    public OutgoingTransferDto requestTransfer(String patientId, String destinationHospital) {
        Patient patient = patientService.getPatientById(patientId);

        String hospitalUrl = validateHospital(destinationHospital);

        String hl7Message = HL7MessageBuilder.buildHL7Message(
                patientMapper.toDto(patient), destinationHospital, TRANSFER_PATIENT, TRANSFER_INCOMING);

        boolean success = iguanaXService.sendHL7Message(hospitalUrl, hl7Message);

        if (success) {
            Patient toBeTransferredPatient = patientService.getPatientById(patientId);
            toBeTransferredPatient.setStatus(TRANSFER_SCHEDULED);
            patientService.updatePatient(toBeTransferredPatient);
        }

        OutgoingTransfer outgoingTransfer = OutgoingTransfer.builder()
                .patient(patient)
                .destination(destinationHospital)
                .submittedOn(LocalDateTime.now())
                .status(success ? "SUCCESS" : "FAILED")
                .build();

        return outgoingTransferMapper.toDto(outgoingTransferRepository.save(outgoingTransfer));
    }

    private String validateHospital(String destinationHospital) {
        String hospitalUrl = hospitalService.getHospitalUrlByName(destinationHospital);
        if (hospitalUrl == null) {
            throw new NotFoundException("Destination hospital not found: " + destinationHospital);
        }
        return hospitalUrl;
    }

    @Override
    public Optional<OutgoingTransferDto> getLatestTransferByPatientId(String patientId) {
        return outgoingTransferRepository.findByPatient_PatientId(patientId)
                .stream()
                .max(Comparator.comparing(OutgoingTransfer::getSubmittedOn)) // Get the latest by transferDate
                .map(outgoingTransferMapper::toDto);
    }

    @Override
    public OutgoingTransferDto completeTransfer(String patientId, String destinationHospital) {
        Patient patient = patientService.getPatientById(patientId);
        patient.setStatus("IN-PATIENT");
        patientService.updatePatient(patient);

        String hospitalUrl = validateHospital(destinationHospital);
        String hl7Message = HL7MessageBuilder.buildHL7Message(
                        patientMapper.toDto(patient), destinationHospital,  UPDATE_PATIENT_INFO, TRANSFER_COMPLETE);
        boolean success = iguanaXService.sendHL7Message(hospitalUrl, hl7Message);

        OutgoingTransfer outgoingTransfer = OutgoingTransfer.builder()
                .patient(patient)
                .destination(destinationHospital)
                .submittedOn(LocalDateTime.now())
                .status(success ? "SUCCESS" : "FAILED")
                .build();

        System.out.println("Transfer marked as COMPLETED for ID: " + patientId);
        return outgoingTransferMapper.toDto(outgoingTransferRepository.save(outgoingTransfer));
    }
}
