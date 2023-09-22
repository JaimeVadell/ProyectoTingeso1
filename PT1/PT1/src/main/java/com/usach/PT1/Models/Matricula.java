package com.usach.PT1.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Matricula")
public class Matricula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMatricula;
    private EPago pago;
    private LocalDate fechaMatricula;
    private int montoMatricula;
    private int numeroCuotas;
    private boolean estadoMatricula;

    @OneToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;
}
