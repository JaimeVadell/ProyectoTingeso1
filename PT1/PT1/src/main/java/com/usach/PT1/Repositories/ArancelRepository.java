package com.usach.PT1.Repositories;

import com.usach.PT1.Models.Arancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArancelRepository extends JpaRepository<Arancel, Long> {
}
