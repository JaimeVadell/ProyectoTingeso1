    package com.usach.PT1.Models;
    
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
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
        @Id @NotBlank
        private String rut_estudiante;
        @NotBlank @Size(max = 50)
        private String nombre;
        @NotBlank @Size(max = 50)
        private String apellido;
        @NotNull
        private LocalDate fechaNacimiento;
        @NotNull
        private ETipoColegio tipoColegio;
        @NotBlank @Size(max = 50)
        private String nombreColegio;
        @NotNull
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
