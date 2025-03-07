package dev.chk.HospitalServiceApplication.repository;

import dev.chk.HospitalServiceApplication.model.OutgoingTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutgoingTransferRepository extends JpaRepository<OutgoingTransfer, Long> {
    List<OutgoingTransfer> findByPatient_PatientId(String patientId);
}