package dev.chk.HospitalServiceApplication.service.impl;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.message.ADT_A02;
import ca.uhn.hl7v2.parser.PipeParser;
import dev.chk.HospitalServiceApplication.constant.ADTMessageTypeConstants;
import dev.chk.HospitalServiceApplication.model.IncomingTransfer;
import dev.chk.HospitalServiceApplication.model.Patient;
import dev.chk.HospitalServiceApplication.repository.IncomingTransferRepository;
import dev.chk.HospitalServiceApplication.service.IncomingTransferService;
import dev.chk.HospitalServiceApplication.service.PatientService;
import dev.chk.HospitalServiceApplication.util.HL7ParserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class IncomingTransferServiceImpl implements IncomingTransferService {
    private final IncomingTransferRepository incomingRepository;
    private final PatientService patientService;
    private final PipeParser parser = new PipeParser();

    public void processHL7Message(String hl7Message) {
        String status = "";
        try {
            Message parsedMessage = parser.parse(hl7Message);

            String messageType = HL7ParserHelper.getMessageType(parsedMessage);
            System.out.println("Received HL7 Message Type: " + messageType);

            if (messageType.contains(ADTMessageTypeConstants.TRANSFER_PATIENT)) {
                System.out.println("Processing ADT^A02: Transfer Incoming");

                ADT_A02 adtMessage = (ADT_A02) parsedMessage;
                String phn = adtMessage.getPID().getPatientIdentifierList(0).getIDNumber().getValue();
                String lastName = adtMessage.getPID().getPatientName(0).getFamilyName().getSurname().getValue();
                String firstName = adtMessage.getPID().getPatientName(0).getGivenName().getValue();
                String dob = adtMessage.getPID().getDateTimeOfBirth().getTime().getValue();
                String gender = adtMessage.getPID().getAdministrativeSex().getValue();
                String address = adtMessage.getPID().getPatientAddress(0).getStreetAddress().getStreetOrMailingAddress().getValue();
                String phone = adtMessage.getPID().getPhoneNumberHome(0).getTelephoneNumber().getValue();

                LocalDate dateOfBirth = LocalDate.parse(dob, DateTimeFormatter.BASIC_ISO_DATE);

                Patient patient;
                try {
                    patient = patientService.getPatientByPhn(phn);
                    patient.setStatus("TRANSFER_INCOMING");
                    patientService.updatePatient(patient);
                } catch (NotFoundException exception) {
                    patient = Patient.builder()
                            .phn(phn)
                            .firstName(firstName)
                            .lastName(lastName)
                            .dob(dateOfBirth)
                            .gender(gender)
                            .address(address)
                            .phone(phone)
                            .build();
                    patient.setStatus("TRANSFER_INCOMING");
                    patientService.addPatient(patient);
                }

                System.out.println("Patient added/updated with TRANSFER_INCOMING status.");

            }
            else if (messageType.contains(ADTMessageTypeConstants.UPDATE_PATIENT_INFO)) {
                System.out.println("Processing ADT^A08: Transfer Completed");

                String phn = extractFieldFromMessage(parsedMessage, "PID-3");
                try {
                    Patient existingPatient = patientService.getPatientByPhn(phn);
                    existingPatient.setStatus("TRANSFER_COMPLETED");
                    patientService.updatePatient(existingPatient);
                    System.out.println("Patient transfer marked as COMPLETED.");
                } catch (NotFoundException exception) {
                    System.out.println("Patient not found, skipping transfer completion update.");
                } catch (Exception exception) {
                    System.out.println(String.format("Error due to %s", exception.getMessage()));
                }
            } else {
                System.out.println("Ignoring non-transfer message: " + messageType);
            }
            status = "SAVED";

        } catch (HL7Exception e) {
            e.printStackTrace();
            System.err.println("Error parsing HL7 Message.");
            status = "ERROR";
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error while processing HL7.");
            status = "ERROR";
        } finally {
            IncomingTransfer messageEntity = IncomingTransfer.builder()
                    .message(hl7Message)
                    .receivedOn(LocalDateTime.now())
                    .status(status)
                    .build();
            incomingRepository.save(messageEntity);
            System.out.println("HL7 Message saved successfully.");
        }
    }
    private static String extractFieldFromMessage(Message parsedMessage, String fieldPath) {
        try {
            Segment pid = (Segment) parsedMessage.get("PID");

            if (fieldPath.equals("PID-3")) {
                String[] pid3Parts = pid.getField(3, 0).encode().split("\\^");
                return pid3Parts[0];
            }
        } catch (HL7Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
