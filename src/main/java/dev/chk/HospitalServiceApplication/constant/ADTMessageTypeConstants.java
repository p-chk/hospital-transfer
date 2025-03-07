package dev.chk.HospitalServiceApplication.constant;

/**
 * Constants for HL7 ADT (Admission, Discharge, Transfer) message types.
 * Naming follows the actual usage instead of generic ADT_A01, ADT_A02.
 */
public class ADTMessageTypeConstants {

    public static final String TRANSFER_PATIENT = "ADT^A02";
    public static final String UPDATE_PATIENT_INFO = "ADT^A08";

    public static final String TRANSFER_SCHEDULED = "TRANSFER_IN-PROGRESS";

    public static final String TRANSFER_INCOMING = "TRANSFER_INCOMING";

    public static final String TRANSFER_COMPLETE = "TRANSFER_COMPLETED";


    /**
     * Utility function to check if a message type is a valid ADT message.
     * @param messageType The HL7 message type (e.g., "ADT^A01").
     * @return true if it is a valid ADT type, false otherwise.
     */
    public static boolean isValidADT(String messageType) {
        return messageType.startsWith("ADT^A");
    }
}
