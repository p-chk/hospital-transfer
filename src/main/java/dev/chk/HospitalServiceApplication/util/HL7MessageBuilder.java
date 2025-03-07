package dev.chk.HospitalServiceApplication.util;

import dev.chk.HospitalServiceApplication.constant.ADTMessageTypeConstants;
import dev.chk.HospitalServiceApplication.dto.PatientDto;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HL7MessageBuilder {

    private static final int ORIGIN = Integer.parseInt(System.getenv("SPRING_ORIGIN_HOSPITAL"));

    public static String buildHL7Message(PatientDto patient, String destinationHospital, String messageType, String transferStatus) {
        if (!ADTMessageTypeConstants.isValidADT(messageType)) {
            throw new IllegalArgumentException("Invalid ADT message type: " + messageType);
        }

        StringBuilder hl7Message = new StringBuilder();

        // MSH Segment
        hl7Message.append("MSH|^~\\&|" + ORIGIN +"|EHR|")
                .append(destinationHospital).append("|EHR|")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))).append("||")
                .append(messageType).append("|").append(patient.getPhn()).append("|P|2.5\r");

        // PID Segment
        hl7Message.append("PID|1||").append(patient.getPhn()).append("^^^" + ORIGIN + "^MRN||")
                .append(patient.getLastName()).append("^").append(patient.getFirstName()).append("||")
                .append(patient.getDob()).append("|")
                .append(patient.getGender().substring(0, 1).toUpperCase()).append("|||")
                .append(patient.getAddress()).append("||")
                .append(patient.getPhone()).append("|||||")
                .append(patient.getPhn()).append("^^^HOSPITALA^SS\r");

        // PV1 Segment - Patient Visit
        hl7Message.append("PV1|1|I|||")
                .append("|||||||||||||||||||||||||||||||||")
                .append(transferStatus.isEmpty() ? "" : transferStatus).append("\r");

        // OBX Segment - Transfer Status Observation (only for UPDATE_PATIENT_INFO)
        if (messageType.equals(ADTMessageTypeConstants.UPDATE_PATIENT_INFO)) {
            hl7Message.append("OBX|1|TX|TRANSFER_STATUS||")
                    .append(transferStatus).append("\r");
        }

        return hl7Message.toString();
    }

}
