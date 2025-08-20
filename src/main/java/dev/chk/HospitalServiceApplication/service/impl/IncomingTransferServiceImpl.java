package dev.chk.HospitalServiceApplication.service.impl;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.message.ADT_A02;
import ca.uhn.hl7v2.parser.PipeParser;
import dev.chk.HospitalServiceApplication.constant.ADTMessageTypeConstants;
import dev.chk.HospitalServiceApplication.dto.PatientDto;
import dev.chk.HospitalServiceApplication.dto.IncomingTransferUpdateDto;
import dev.chk.HospitalServiceApplication.mapper.PatientMapper;
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
    public static final String TRANSFER_INCOMING = "TRANSFER_INCOMING";
    public static final String DISCHARGED = "DISCHARGED";
    private final IncomingTransferRepository incomingRepository;
    private final PatientService patientService;

    private final PatientMapper patientMapper;
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
                persistPatient(phn, lastName, firstName, gender, address, phone, dateOfBirth, TRANSFER_INCOMING);

                System.out.println("Patient added/updated with TRANSFER_INCOMING status.");

            }
            else if (messageType.contains(ADTMessageTypeConstants.UPDATE_PATIENT_INFO)) {
                System.out.println("Processing ADT^A08: Transfer Completed");

                String phn = extractFieldFromMessage(parsedMessage, "PID-3");
                try {
                    Patient existingPatient = patientService.getPatientByPhn(phn);
                    existingPatient.setStatus(DISCHARGED);
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

    private Patient persistPatient(String phn,
                                   String lastName,
                                   String firstName,
                                   String gender,
                                   String address,
                                   String phone,
                                   LocalDate dateOfBirth, String status) {
        Patient patient;
        try {
            patient = patientService.getPatientByPhn(phn);
            patient.setStatus(status);
            return patientService.updatePatient(patient);
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
            patient.setStatus(status);
            return patientService.addPatient(patient);
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


    public void requestTransfer(PatientDto patientDto) {
        String status = "";
        try {
            patientMapper.toDto(persistPatient(patientDto.getPhn(),
                    patientDto.getLastName(),
                    patientDto.getFirstName(),
                    patientDto.getGender(),
                    patientDto.getAddress(),
                    patientDto.getPhone(),
                    LocalDate.parse(patientDto.getDob(), DateTimeFormatter.BASIC_ISO_DATE),
                    TRANSFER_INCOMING));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error while processing JSON");
            status = "ERROR";
        } finally {
            saveMessage(patientDto, status);
        }
    }

    private void saveMessage(PatientDto patientDto, String status) {
        IncomingTransfer messageEntity = IncomingTransfer.builder()
                .message(patientDto.toString())
                .receivedOn(LocalDateTime.now())
                .status(status)
                .build();
        incomingRepository.save(messageEntity);
        System.out.println("JSON Message saved successfully.");
    }

    public void completeTransfer(PatientDto patientDto) {
        String status = "";
        try {
            patientMapper.toDto(persistPatient(patientDto.getPhn(),
                    patientDto.getLastName(),
                    patientDto.getFirstName(),
                    patientDto.getGender(),
                    patientDto.getAddress(),
                    patientDto.getPhone(),
                    LocalDate.parse(patientDto.getDob(), DateTimeFormatter.BASIC_ISO_DATE),
                    TRANSFER_INCOMING));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error while processing JSON");
            status = "ERROR";
        } finally {
            saveMessage(patientDto, status);
        }
    }

    @Override
    public PatientDto updateIncomingTransferWithDetails(Long transferId, IncomingTransferUpdateDto updateDto) {
        try {
            // Find the incoming transfer record
            IncomingTransfer existingTransfer = incomingRepository.findByTransferId(transferId)
                    .orElseThrow(() -> new NotFoundException("Incoming transfer not found with id: " + transferId));
            
            // Update the patient information using the detailed DTO
            Patient updatedPatient = persistPatient(
                    updateDto.getPhn(),
                    updateDto.getLastName(),
                    updateDto.getFirstName(),
                    updateDto.getGender(),
                    updateDto.getAddress(),
                    updateDto.getPhone(),
                    LocalDate.parse(updateDto.getDob(), DateTimeFormatter.BASIC_ISO_DATE),
                    updateDto.getStatus() != null ? updateDto.getStatus() : TRANSFER_INCOMING
            );
            
            // Update the transfer record with enhanced information
            existingTransfer.setStatus("UPDATED");
            existingTransfer.setMessage(String.format("Updated transfer - PHN: %s, Status: %s, Notes: %s, Updated by: %s",
                    updateDto.getPhn(), 
                    updateDto.getStatus(),
                    updateDto.getNotes() != null ? updateDto.getNotes() : "No notes",
                    updateDto.getUpdatedBy() != null ? updateDto.getUpdatedBy() : "System"));
            incomingRepository.save(existingTransfer);
            
            System.out.println("Incoming transfer updated with details for transfer ID: " + transferId);
            
            return patientMapper.toDto(updatedPatient);
            
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating incoming transfer with details: " + e.getMessage());
        }
    }

    @Override
    public PatientDto updateIncomingTransfer(Long transferId, PatientDto patientDto) {
        String status = "";
        try {
            // Find the incoming transfer record
            IncomingTransfer existingTransfer = incomingRepository.findByTransferId(transferId)
                    .orElseThrow(() -> new NotFoundException("Incoming transfer not found with id: " + transferId));
            
            // Update the patient information
            Patient updatedPatient = persistPatient(
                    patientDto.getPhn(),
                    patientDto.getLastName(),
                    patientDto.getFirstName(),
                    patientDto.getGender(),
                    patientDto.getAddress(),
                    patientDto.getPhone(),
                    LocalDate.parse(patientDto.getDob(), DateTimeFormatter.BASIC_ISO_DATE),
                    patientDto.getStatus() != null ? patientDto.getStatus() : TRANSFER_INCOMING
            );
            
            // Update the transfer record
            existingTransfer.setStatus("UPDATED");
            existingTransfer.setMessage(patientDto.toString());
            incomingRepository.save(existingTransfer);
            
            status = "UPDATED";
            System.out.println("Incoming transfer updated successfully for transfer ID: " + transferId);
            
            return patientMapper.toDto(updatedPatient);
            
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            status = "ERROR";
            throw new RuntimeException("Error updating incoming transfer: " + e.getMessage());
        }
    }

    @Override
    public PatientDto getIncomingTransferById(Long transferId) {
        try {
            IncomingTransfer transfer = incomingRepository.findByTransferId(transferId)
                    .orElseThrow(() -> new NotFoundException("Incoming transfer not found with id: " + transferId));
            
            // Parse the message to extract patient information
            // For simplicity, we'll try to get patient from the stored message
            // In a real scenario, you might want to store patient ID in the transfer record
            
            // For now, return a basic response - you might want to enhance this
            return PatientDto.builder()
                    .status(transfer.getStatus())
                    .build();
                    
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving incoming transfer: " + e.getMessage());
        }
    }
}
