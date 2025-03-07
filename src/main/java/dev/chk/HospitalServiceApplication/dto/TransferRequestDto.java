package dev.chk.HospitalServiceApplication.dto;

import lombok.Data;

@Data
public class TransferRequestDto {
    private String patientId;
    private String destinationHospital;
}
