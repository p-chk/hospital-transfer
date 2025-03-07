package dev.chk.HospitalServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hospital implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hospitalSeqnum", nullable = false)
    private Long hospitalSeqnum;

    private String hospitalName;

    private String hospitalTransferQueue;

}
