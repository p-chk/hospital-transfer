package dev.chk.HospitalServiceApplication.mapper;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDate.class, DateTimeFormatter.class})
public interface PatientMapper {

    String HL7_DATE_FORMAT = "yyyyMMdd";

    @Mapping(target = "dob", source = "dob", dateFormat = HL7_DATE_FORMAT)
    PatientDto toDto(Patient patient);

    @Mapping(target = "dob", expression = "java(patientDto.getDob() != null ? LocalDate.parse(patientDto.getDob(), DateTimeFormatter.ofPattern(HL7_DATE_FORMAT)) : null)")
    Patient toEntity(PatientDto patientDto);
}
