package com.usach.PT1.Repositories;

import com.usach.PT1.Models.Cuota;
import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotasRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByEstudianteAndPagadaIsFalseOrderByPlazoMaximoPagoAsc(Estudiante estudiante);

}
