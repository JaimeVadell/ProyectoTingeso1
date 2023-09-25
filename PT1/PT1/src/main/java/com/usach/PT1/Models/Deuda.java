package com.usach.PT1.Models;

import jakarta.persistence.*;
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

    private int montoDeuda;
    private int CuotasRestantes;
    private int precioCuota;
    private int cuotasConRetraso;
    @OneToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;

}
