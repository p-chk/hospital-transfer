package dev.chk.HospitalServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patient_seqnum", nullable = false)
    private Long patientSeqnum;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    private String phn;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String gender;

    private String address;

    private String phone;

    private String insuranceId;

    private String status;

}
