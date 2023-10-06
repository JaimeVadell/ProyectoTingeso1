package com.usach.PT1.Repositories;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByEstudiante(Estudiante estudiante);
}
