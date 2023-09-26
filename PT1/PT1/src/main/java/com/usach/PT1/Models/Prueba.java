package com.usach.PT1.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "Pruebas")
public class Prueba {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long IdPrueba;

    @NotNull
    @Max(value = 1000, message = "El puntaje máximo permitido es 100")
    @Min(value = 0, message = "El puntaje mínimo permitido es 0")
    private int puntaje;
    @NotNull
    private LocalDate diaPrueba;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;

}
