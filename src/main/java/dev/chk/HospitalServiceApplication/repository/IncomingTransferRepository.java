package dev.chk.HospitalServiceApplication.repository;

import dev.chk.HospitalServiceApplication.model.IncomingTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomingTransferRepository extends JpaRepository<IncomingTransfer, Long> {
    
    @Query("SELECT it FROM IncomingTransfer it WHERE it.incomingTransferSeqnum = :id")
    Optional<IncomingTransfer> findByTransferId(@Param("id") Long id);
    
    @Query("SELECT it FROM IncomingTransfer it WHERE it.status = :status ORDER BY it.receivedOn DESC")
    Optional<IncomingTransfer> findLatestByStatus(@Param("status") String status);
}