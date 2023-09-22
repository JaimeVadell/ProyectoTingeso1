package com.usach.PT1.Repositories;

import com.usach.PT1.Models.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeudaRepository extends JpaRepository<Deuda, Long> {
}
