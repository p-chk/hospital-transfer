package dev.chk.HospitalServiceApplication.configurarion;

import dev.chk.HospitalServiceApplication.listener.IncomingTransferListener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HL7ServerConfig {

    @Bean
    public ApplicationRunner startHL7Server(IncomingTransferListener incomingTransferListener) {
        return args -> incomingTransferListener.startServer();
    }
}
