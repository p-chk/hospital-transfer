package dev.chk.HospitalServiceApplication.service;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.dto.IncomingTransferUpdateDto;
import dev.chk.HospitalServiceApplication.model.IncomingTransfer;
import dev.chk.HospitalServiceApplication.repository.IncomingTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public interface IncomingTransferService {

    void processHL7Message(String hl7Message);

    void requestTransfer(PatientDto patientDto);

    void completeTransfer(PatientDto patientDto);

    PatientDto updateIncomingTransfer(Long transferId, PatientDto patientDto);

    PatientDto updateIncomingTransferWithDetails(Long transferId, IncomingTransferUpdateDto updateDto);

    PatientDto getIncomingTransferById(Long transferId);
}
