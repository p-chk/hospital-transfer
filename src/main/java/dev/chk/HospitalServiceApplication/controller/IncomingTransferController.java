package dev.chk.HospitalServiceApplication.controller;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.dto.TransferRequestDto;
import dev.chk.HospitalServiceApplication.dto.IncomingTransferUpdateDto;
import dev.chk.HospitalServiceApplication.service.IncomingTransferService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incoming-transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IncomingTransferController {

    private final IncomingTransferService incomingTransferService;

    @PostMapping("/request-transfer")
    public ResponseEntity<PatientDto> requestTransfer(
            @RequestBody PatientDto transferRequest) {
        incomingTransferService.requestTransfer(transferRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/complete-transfer")
    public ResponseEntity<PatientDto> completeTransfer(@RequestBody PatientDto completeTransfer) {
        incomingTransferService.completeTransfer(completeTransfer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/update-transfer/{id}")
    public ResponseEntity<PatientDto> updateIncomingTransfer(
            @PathVariable Long id,
            @Valid @RequestBody PatientDto patientDto) {
        PatientDto updatedPatient = incomingTransferService.updateIncomingTransfer(id, patientDto);
        return ResponseEntity.ok(updatedPatient);
    }

    @PutMapping("/update-transfer-details/{id}")
    public ResponseEntity<PatientDto> updateIncomingTransferWithDetails(
            @PathVariable Long id,
            @Valid @RequestBody IncomingTransferUpdateDto updateDto) {
        PatientDto updatedPatient = incomingTransferService.updateIncomingTransferWithDetails(id, updateDto);
        return ResponseEntity.ok(updatedPatient);
    }

    @GetMapping("/transfer/{id}")
    public ResponseEntity<PatientDto> getIncomingTransfer(@PathVariable Long id) {
        PatientDto transfer = incomingTransferService.getIncomingTransferById(id);
        return ResponseEntity.ok(transfer);
    }
}
