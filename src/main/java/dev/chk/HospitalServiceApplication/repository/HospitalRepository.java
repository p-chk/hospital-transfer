package dev.chk.HospitalServiceApplication.repository;

import dev.chk.HospitalServiceApplication.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, String> {
    Optional<Hospital> findByHospitalName(String destinationHospital);
}