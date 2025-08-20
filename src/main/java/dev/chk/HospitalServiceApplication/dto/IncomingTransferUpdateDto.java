package dev.chk.HospitalServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomingTransferUpdateDto {
    
    private Long transferId;
    
    @NotBlank(message = "PHN is required")
    private String phn;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be in YYYY-MM-DD format")
    private String dob;
    
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String gender;
    
    private String address;
    private String phone;
    private String insuranceId;
    
    @Pattern(regexp = "^(TRANSFER_INCOMING|DISCHARGED|UPDATED)$", 
             message = "Status must be TRANSFER_INCOMING, DISCHARGED, or UPDATED")
    private String status;
    
    private String notes;
    private LocalDateTime lastUpdated;
    private String updatedBy;
}
