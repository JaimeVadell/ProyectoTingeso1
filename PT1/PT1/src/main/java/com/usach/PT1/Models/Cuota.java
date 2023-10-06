package com.usach.PT1.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private int montoCuota;

    @NotNull
    private LocalDate plazoMaximoPago;

    @NotNull
    private boolean pagada;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;

    @OneToOne(mappedBy = "cuotaPagada")
    private Pago pago;
}
