package com.usach.PT1.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private EPago pago;
    @NotNull
    private LocalDate fechaMatricula;
    @NotNull
    private int montoMatricula;
    @NotNull
    private int numeroCuotas;
    @NotNull
    private boolean estadoMatricula;

    @OneToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;
}
