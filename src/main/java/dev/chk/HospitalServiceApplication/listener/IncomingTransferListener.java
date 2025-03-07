package dev.chk.HospitalServiceApplication.listener;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import ca.uhn.hl7v2.protocol.ReceivingApplicationException;
import dev.chk.HospitalServiceApplication.service.IncomingTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IncomingTransferListener {

    private static final int HL7_PORT = 65433;
    private final IncomingTransferService incomingTransferService;
    private SimpleServer server;
    private boolean isRunning = false; // Prevent multiple instances

    @PostConstruct
    public void startServer() {
        new Thread(() -> {
            if (isRunning) {
                System.out.println("HL7 Listener is already running. Skipping startup.");
                return;
            }
            try {
                releasePort(HL7_PORT);
                waitForPortRelease();
                System.out.println("Starting HL7 MLLP Listener on port " + HL7_PORT);
                server = new SimpleServer(HL7_PORT);
                server.registerApplication("*", "*", new HL7IncomingHandler(incomingTransferService));
                server.start();
                isRunning = true;
                System.out.println("HL7 Listener started successfully!");
            } catch (Exception e) {
                System.err.println("Error starting HL7 listener: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    @PreDestroy
    public void stopServer() {
        if (server != null && isRunning) {
            try {
                System.out.println("Shutting down HL7 Listener...");
                server.stop();
                releasePort(HL7_PORT);
                isRunning = false;
                System.out.println("HL7 Listener shut down successfully.");
            } catch (Exception e) {
                System.err.println("Error shutting down HL7 Listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void waitForPortRelease() {
        int retries = 10;
        while (retries > 0) {
            try (ServerSocket socket = new ServerSocket(HL7_PORT)) {
                System.out.println("Port " + HL7_PORT + " is now available.");
                break;
            } catch (IOException e) {
                System.out.println("Waiting for port " + HL7_PORT + " to be released...");
                retries--;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void releasePort(int port) {
        try {
            System.out.println("Forcefully releasing port: " + port);
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    try (Socket socket = new Socket(inetAddress, port)) {
                        socket.close();
                        System.out.println("Closed lingering connection on " + inetAddress.getHostAddress());
                    } catch (IOException ignored) {}
                }
            }
            System.out.println("All lingering connections released.");
        } catch (IOException e) {
            System.err.println("Error releasing port: " + e.getMessage());
        }
    }

    private static class HL7IncomingHandler implements ReceivingApplication<Message> {
        private final PipeParser parser = new PipeParser();
        private final IncomingTransferService incomingTransferService;

        public HL7IncomingHandler(IncomingTransferService incomingTransferService) {
            this.incomingTransferService = incomingTransferService;
        }

        @Override
        public Message processMessage(Message message, Map<String, Object> metadata)
                throws ReceivingApplicationException, HL7Exception {
            try {
                String hl7String = parser.encode(message);
                System.out.println("Received HL7 Message:\n" + hl7String);
                incomingTransferService.processHL7Message(hl7String);
                return message.generateACK();
            } catch (Exception e) {
                throw new ReceivingApplicationException("Error processing HL7", e);
            }
        }

        @Override
        public boolean canProcess(Message message) {
            return true;
        }
    }
}
