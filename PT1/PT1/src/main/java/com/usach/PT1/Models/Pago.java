package com.usach.PT1.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Pago")
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPago;
    private int montoPagado;
    private LocalDate fechaPago;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;

}
