package dev.chk.HospitalServiceApplication.service.impl;

import dev.chk.HospitalServiceApplication.model.Hospital;
import dev.chk.HospitalServiceApplication.repository.HospitalRepository;
import dev.chk.HospitalServiceApplication.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public String getHospitalUrlByName(String hospitalName) {
        return hospitalRepository.findByHospitalName(hospitalName)
                .map(Hospital::getHospitalTransferQueue)
                .orElseThrow(() -> new NotFoundException("Hospital " + hospitalName + " not found."));
    }
}
