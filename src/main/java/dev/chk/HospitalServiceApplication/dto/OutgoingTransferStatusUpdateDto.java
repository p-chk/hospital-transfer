package dev.chk.HospitalServiceApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutgoingTransferStatusUpdateDto {
    
    @NotNull(message = "Transfer ID is required")
    private Long transferId;
    
    @NotBlank(message = "Status is required")
    private String status;
}
