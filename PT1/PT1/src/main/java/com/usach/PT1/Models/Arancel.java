package com.usach.PT1.Models;

import jakarta.persistence.*;
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
public class Arancel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idArancel;
    @NotNull
    private EMedioPago pago;
    @NotNull
    private LocalDate fechaCreacionArancel;
    @NotNull
    private int montoTotalArancel;
    @NotNull
    private int numeroCuotas;
    @NotNull
    private boolean estadoDePagoArancel;


    @OneToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;
}
