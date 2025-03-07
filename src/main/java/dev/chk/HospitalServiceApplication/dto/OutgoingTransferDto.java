package dev.chk.HospitalServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutgoingTransferDto {
    private Long transferId;
    private String patientId;
    private String destination;
    private LocalDateTime submittedOn;
    private String status;
}