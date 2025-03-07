package dev.chk.HospitalServiceApplication.mapper;

import dev.chk.HospitalServiceApplication.dto.OutgoingTransferDto;
import dev.chk.HospitalServiceApplication.model.OutgoingTransfer;
import dev.chk.HospitalServiceApplication.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = PatientMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OutgoingTransferMapper {

    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "outgoingTransferSeqnum", target = "transferId")
    OutgoingTransferDto toDto(OutgoingTransfer transfer);

}
