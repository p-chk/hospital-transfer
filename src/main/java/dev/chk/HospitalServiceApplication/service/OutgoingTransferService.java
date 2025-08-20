package dev.chk.HospitalServiceApplication.service;

import dev.chk.HospitalServiceApplication.dto.OutgoingTransferDto;
import dev.chk.HospitalServiceApplication.dto.OutgoingTransferStatusUpdateDto;

import java.util.List;
import java.util.Optional;

public interface OutgoingTransferService {
    OutgoingTransferDto requestTransfer(String patientId, String destinationHospital);

    Optional<OutgoingTransferDto> getLatestTransferByPatientId(String patientId);

    OutgoingTransferDto completeTransfer(String patientId, String destinationHospital);
    
    OutgoingTransferDto updateTransferStatus(OutgoingTransferStatusUpdateDto statusUpdateDto);
}
