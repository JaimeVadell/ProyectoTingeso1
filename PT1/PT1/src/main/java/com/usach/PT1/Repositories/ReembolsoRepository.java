package com.usach.PT1.Repositories;

import com.usach.PT1.Models.Estudiante;
import com.usach.PT1.Models.Reembolso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReembolsoRepository extends JpaRepository<Reembolso, Long> {
    Optional<Reembolso> findByEstudiante(Estudiante estudiante);

}
