package com.usach.PT1.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private int montoPagado;
    @NotNull
    private LocalDate fechaPago;
    @NotNull
    private ETipoPago tipoPago;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;

    @OneToOne
    @JoinColumn(name = "cuota_id", referencedColumnName = "idCuota")
    private Cuota cuotaPagada;


}
