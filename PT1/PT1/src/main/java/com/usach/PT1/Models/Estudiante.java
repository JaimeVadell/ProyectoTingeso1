package com.usach.PT1.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Estudiante")
public class Estudiante {
    @Id
    private String rut_estudiante;

    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private ETipoColegio tipoColegio;
    private String nombreColegio;
    private LocalDate anioEgreso;
    @OneToMany(mappedBy = "estudiante")
    private List<Prueba> pruebas;

    @OneToOne(mappedBy = "estudiante")
    private Matricula matricula;

    @OneToMany(mappedBy = "estudiante")
    private List<Pago> pagos;

    @OneToOne(mappedBy = "estudiante")
    private Deuda deuda;




}
