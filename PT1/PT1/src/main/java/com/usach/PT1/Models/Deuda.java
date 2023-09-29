package com.usach.PT1.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Deuda")
public class Deuda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDeuda;

    @NotNull
    private int montoDeuda;
    @NotNull
    private int CuotasRestantes;
    @NotNull
    private int precioCuota;
    @NotNull
    private int precioCuotaInicial;
    @NotNull
    private int cuotasConRetraso;
    @NotNull
    private int cuotasConRetrasoHistorico;

    @OneToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;


}
