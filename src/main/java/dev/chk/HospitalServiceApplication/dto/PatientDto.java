package dev.chk.HospitalServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {
    private String patientId;
    private String phn;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String address;
    private String phone;
    private String insuranceId;
    private String status;
}