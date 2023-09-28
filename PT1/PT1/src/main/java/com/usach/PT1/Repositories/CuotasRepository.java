package com.usach.PT1.Repositories;

import com.usach.PT1.Models.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuotasRepository extends JpaRepository<Cuota, Long> {
}
