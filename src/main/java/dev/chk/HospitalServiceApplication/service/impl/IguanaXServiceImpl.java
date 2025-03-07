package dev.chk.HospitalServiceApplication.service.impl;

import dev.chk.HospitalServiceApplication.service.IguanaXService;
import org.springframework.stereotype.Service;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Service
public class IguanaXServiceImpl implements IguanaXService {

    @Override
    public boolean sendHL7Message(String hospitalUrl, String hl7Message) {
        try {
            String[] hostParts = hospitalUrl.split(":");
            String host = hostParts[0];
            int port = Integer.parseInt(hostParts[1]);

            String mllpMessage = "\u000b" + hl7Message + "\u001c\r";

            try (Socket socket = new Socket(host, port);
                 OutputStream outputStream = socket.getOutputStream();
                 InputStream inputStream = socket.getInputStream();
                 PrintWriter writer = new PrintWriter(outputStream, true, StandardCharsets.UTF_8)) {

                writer.print(mllpMessage);
                writer.flush();
                System.out.println("Sent HL7 Message:\n" + hl7Message);

                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                String response = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

                System.out.println("Received HL7 Response:\n" + response);

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
