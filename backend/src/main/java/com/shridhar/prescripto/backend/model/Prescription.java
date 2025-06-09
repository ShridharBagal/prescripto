package com.shridhar.prescripto.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User doctor;

    @ManyToOne
    private User patient;

    private LocalDateTime dateIssued;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private List<Medication> medications;
}
