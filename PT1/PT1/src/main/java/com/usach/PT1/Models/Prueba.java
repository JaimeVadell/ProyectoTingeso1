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
@Table(name = "Pruebas")
public class Prueba {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long IdPrueba;
    private int puntaje;
    private LocalDate diaPrueba;

    @ManyToOne
    @JoinColumn(name = "rut_estudiante", referencedColumnName = "rut_estudiante")
    private Estudiante estudiante;

}
