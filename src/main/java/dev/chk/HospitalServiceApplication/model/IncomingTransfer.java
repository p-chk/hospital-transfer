package dev.chk.HospitalServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomingTransfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "incomingTransferSeqnum", nullable = false)
    private Long incomingTransferSeqnum;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    private LocalDateTime receivedOn;

    private String status;
}
