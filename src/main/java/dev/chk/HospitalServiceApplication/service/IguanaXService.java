package dev.chk.HospitalServiceApplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public interface IguanaXService {

    boolean sendHL7Message(String hospitalUrl, String hl7Message);

}
