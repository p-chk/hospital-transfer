package dev.chk.HospitalServiceApplication.controller;

import dev.chk.HospitalServiceApplication.dto.OutgoingTransferDto;
import dev.chk.HospitalServiceApplication.dto.TransferRequestDto;
import dev.chk.HospitalServiceApplication.service.OutgoingTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/outgoing-transfers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OutgoingTransferController {

    private final OutgoingTransferService outgoingTransferService;

    @PostMapping("/request-transfer")
    public ResponseEntity<OutgoingTransferDto> requestTransfer(
            @RequestBody TransferRequestDto transferRequest) {

        OutgoingTransferDto transferDto = outgoingTransferService.requestTransfer(
                transferRequest.getPatientId(),
                transferRequest.getDestinationHospital());
        return ResponseEntity.status(HttpStatus.CREATED).body(transferDto);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<OutgoingTransferDto> getLatestTransferByPatientId(@PathVariable String id) {
        return outgoingTransferService.getLatestTransferByPatientId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/complete-transfer")
    public ResponseEntity<OutgoingTransferDto> completeTransfer(@RequestBody TransferRequestDto transferRequest) {
        OutgoingTransferDto transferDto = outgoingTransferService.completeTransfer(transferRequest.getPatientId(),
                transferRequest.getDestinationHospital());
        return ResponseEntity.status(HttpStatus.OK).body(transferDto);
    }
}
