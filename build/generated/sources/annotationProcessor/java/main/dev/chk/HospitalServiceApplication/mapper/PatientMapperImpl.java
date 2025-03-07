package dev.chk.HospitalServiceApplication.mapper;

import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.model.Patient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-05T05:21:28-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.1.jar, environment: Java 11.0.15 (Eclipse Adoptium)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyyMMdd_0276306848 = DateTimeFormatter.ofPattern( "yyyyMMdd" );

    @Override
    public PatientDto toDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientDto.PatientDtoBuilder patientDto = PatientDto.builder();

        if ( patient.getDob() != null ) {
            patientDto.dob( dateTimeFormatter_yyyyMMdd_0276306848.format( patient.getDob() ) );
        }
        patientDto.patientId( patient.getPatientId() );
        patientDto.phn( patient.getPhn() );
        patientDto.firstName( patient.getFirstName() );
        patientDto.lastName( patient.getLastName() );
        patientDto.gender( patient.getGender() );
        patientDto.address( patient.getAddress() );
        patientDto.phone( patient.getPhone() );
        patientDto.insuranceId( patient.getInsuranceId() );
        patientDto.status( patient.getStatus() );

        return patientDto.build();
    }

    @Override
    public Patient toEntity(PatientDto patientDto) {
        if ( patientDto == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.patientId( patientDto.getPatientId() );
        patient.phn( patientDto.getPhn() );
        patient.firstName( patientDto.getFirstName() );
        patient.lastName( patientDto.getLastName() );
        patient.gender( patientDto.getGender() );
        patient.address( patientDto.getAddress() );
        patient.phone( patientDto.getPhone() );
        patient.insuranceId( patientDto.getInsuranceId() );
        patient.status( patientDto.getStatus() );

        patient.dob( patientDto.getDob() != null ? LocalDate.parse(patientDto.getDob(), DateTimeFormatter.ofPattern(HL7_DATE_FORMAT)) : null );

        return patient.build();
    }
}
