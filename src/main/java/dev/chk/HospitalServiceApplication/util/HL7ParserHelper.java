package dev.chk.HospitalServiceApplication.util;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.HL7Exception;

public class HL7ParserHelper {

    public static String getMessageType(Message parsedMessage) throws HL7Exception {
        // Get the MSH segment
        Segment msh = (Segment) parsedMessage.get("MSH");

        // Extract Message Code (MSH-9-1) and Trigger Event (MSH-9-2)
        String messageCode = msh.getField(9, 0).toString();  // MSH-9-1 (ADT)
        String triggerEvent = msh.getField(9, 1).toString(); // MSH-9-2 (A02, A08, etc.)

        return messageCode + "^" + triggerEvent;
    }
}
