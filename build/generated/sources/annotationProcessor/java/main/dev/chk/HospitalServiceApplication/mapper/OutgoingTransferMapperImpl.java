package dev.chk.HospitalServiceApplication.mapper;

import dev.chk.HospitalServiceApplication.dto.OutgoingTransferDto;
import dev.chk.HospitalServiceApplication.model.OutgoingTransfer;
import dev.chk.HospitalServiceApplication.model.Patient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T05:21:28-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.1.jar, environment: Java 11.0.15 (Eclipse Adoptium)"
)
@Component
public class OutgoingTransferMapperImpl implements OutgoingTransferMapper {

    @Override
    public OutgoingTransferDto toDto(OutgoingTransfer transfer) {
        if ( transfer == null ) {
            return null;
        }

        OutgoingTransferDto.OutgoingTransferDtoBuilder outgoingTransferDto = OutgoingTransferDto.builder();

        outgoingTransferDto.patientId( transferPatientPatientId( transfer ) );
        outgoingTransferDto.transferId( transfer.getOutgoingTransferSeqnum() );
        outgoingTransferDto.destination( transfer.getDestination() );
        outgoingTransferDto.submittedOn( transfer.getSubmittedOn() );
        outgoingTransferDto.status( transfer.getStatus() );

        return outgoingTransferDto.build();
    }

    private String transferPatientPatientId(OutgoingTransfer outgoingTransfer) {
        if ( outgoingTransfer == null ) {
            return null;
        }
        Patient patient = outgoingTransfer.getPatient();
        if ( patient == null ) {
            return null;
        }
        String patientId = patient.getPatientId();
        if ( patientId == null ) {
            return null;
        }
        return patientId;
    }
}
