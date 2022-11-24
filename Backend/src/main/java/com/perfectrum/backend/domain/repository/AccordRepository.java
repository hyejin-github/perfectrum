package com.perfectrum.backend.domain.repository;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.AccordEntity;
import com.perfectrum.backend.domain.entity.PerfumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccordRepository extends JpaRepository<AccordEntity,Integer> {

    AccordEntity findByIdx(Integer accordIdx);

    List<AccordEntity> findByAccordClass(AccordClassEntity idx);

    @Query("SELECT COUNT(a) FROM AccordEntity AS a" +
            " WHERE a.idx IN (SELECT pa.accord FROM PerfumeAccordsEntity AS pa WHERE pa.perfume =:perfumeEntity) AND a.accordClass =:accordClassEntity")
    Integer countByAccordClass(PerfumeEntity perfumeEntity, AccordClassEntity accordClassEntity);
}
