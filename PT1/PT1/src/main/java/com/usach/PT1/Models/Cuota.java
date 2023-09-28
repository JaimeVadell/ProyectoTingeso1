package com.usach.PT1.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Cuota")
public class Cuota {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCuota;

    private int montoCuota;

    private LocalDate plazoMaximoPago;

    private boolean pagada;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;
}
