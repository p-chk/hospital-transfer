package dev.chk.HospitalServiceApplication.service;

import dev.chk.HospitalServiceApplication.model.Hospital;
import dev.chk.HospitalServiceApplication.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HospitalService {

    List<Hospital> getAllHospitals();

    String getHospitalUrlByName(String hospitalName);
}
