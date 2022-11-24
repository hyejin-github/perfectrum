package com.perfectrum.backend.domain.repository;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.AccordEntity;
import com.perfectrum.backend.domain.entity.PerfumeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PerfumeRepository extends JpaRepository<PerfumeEntity, Integer> {

    List<PerfumeEntity> findTop6ByOrderByItemRatingDesc();
    List<PerfumeEntity> findTop6ByTimezoneAndSeasonsContainsOrderByItemRatingDesc(String timeZone, String season);

    List<PerfumeEntity> findTop20ByTimezoneAndSeasonsContainsOrderByItemRatingDesc(String timeZone, String season);
    List<PerfumeEntity> findAllByGenderAndLongevity(String gender,Integer longevity);

    @Query(value =
            "SELECT p FROM PerfumeEntity AS p WHERE p.gender = :gender AND p.seasons like %:season% and " +
                    "p.idx in (SELECT pa.perfume FROM PerfumeAccordsEntity AS pa " +
                    "WHERE pa.accord IN (SELECT a.idx from AccordEntity AS a WHERE a.accordClass = :accordClassEntity)) " +
                    "ORDER BY p.itemRating DESC")
    List<PerfumeEntity> findBest6Perfumes(String gender, String season, AccordClassEntity accordClassEntity, Pageable top6);

    PerfumeEntity findByIdx(Integer idx);

    @Query(value = " select a from AccordEntity as a where a.idx in " +
            "(select p.accord from PerfumeAccordsEntity as p where p.perfume = :perfume)")
    List<AccordEntity> findByPerfume(PerfumeEntity perfume);

    @Query("SELECT p FROM PerfumeEntity AS p " +
            "WHERE p.seasons LIKE %:season% AND p.longevity IN (1,2,3)" +
            "AND p.idx IN (SELECT pa.perfume FROM PerfumeAccordsEntity AS pa " +
            "WHERE pa.accord IN (SELECT a.idx FROM AccordEntity AS a WHERE a.accordClass = :accordClass))")
    List<PerfumeEntity> findBySeasonAndWeakLongevityAndAccordClass(String season, AccordClassEntity accordClass);

    @Query("SELECT p FROM PerfumeEntity AS p " +
            "WHERE p.gender = :gender AND p.seasons LIKE %:season% AND p.longevity IN (1,2,3)" +
            "AND p.idx IN (SELECT pa.perfume FROM PerfumeAccordsEntity AS pa " +
            "WHERE pa.accord IN (SELECT a.idx FROM AccordEntity AS a WHERE a.accordClass = :accordClass))")
    List<PerfumeEntity> findByGenderAndSeasonAndWeakLongevityAndAccordClass(String gender, String season, AccordClassEntity accordClass);

    @Query("SELECT p FROM PerfumeEntity AS p " +
            "WHERE p.seasons LIKE %:season% AND p.longevity IN (4,5)" +
            "AND p.idx IN (SELECT pa.perfume FROM PerfumeAccordsEntity AS pa " +
            "WHERE pa.accord IN (SELECT a.idx FROM AccordEntity AS a WHERE a.accordClass = :accordClass))")
    List<PerfumeEntity> findBySeasonAndStrongLongevityAndAccordClass(String season, AccordClassEntity accordClass);

    @Query("SELECT p FROM PerfumeEntity AS p " +
            "WHERE p.gender = :gender AND p.seasons LIKE %:season% AND p.longevity IN (4,5)" +
            "AND p.idx IN (SELECT pa.perfume FROM PerfumeAccordsEntity AS pa " +
            "WHERE pa.accord IN (SELECT a.idx FROM AccordEntity AS a WHERE a.accordClass = :accordClass))")
    List<PerfumeEntity> findByGenderAndSeasonAndStrongLongevityAndAccordClass(String gender, String season, AccordClassEntity accordClass);

    @Query(value = "select p from PerfumeEntity p " +
            "where p.gender in (:gender)" +
            "and p.longevity in (:longevity) " +
            "and p.idx IN(select pa.perfume from PerfumeAccordsEntity as pa " +
            "where pa.accord in (select a.idx from AccordEntity a " +
            "where a.accordClass in(:accordclass))) and p.idx < :lastIdx " +
            "group by p.perfumeImg " +
            "order by p.itemRating desc, p.idx desc")
    Slice<PerfumeEntity> findAllByGenderAndLongevityAndAccordClass(List<String> gender, List<Integer> longevity, List<AccordClassEntity> accordclass, Integer lastIdx, Pageable pageable);

    @Query(value = "select p from PerfumeEntity p " +
            "where p.gender in (:gender)" +
            "and p.longevity in (:longevity) " +
            "and p.idx IN(select pa.perfume from PerfumeAccordsEntity as pa " +
            "where pa.accord in (select a.idx from AccordEntity a " +
            "where a.accordClass in(:accordclass))) " +
//            "group by p.perfumeImg " +
            "order by p.itemRating desc, p.idx desc")
    List<PerfumeEntity> findAllByGenderAndLongevityAndAccordClass2(List<String> gender, List<Integer> longevity, List<AccordClassEntity> accordclass);


    PerfumeEntity findTop1ByOrderByIdxDesc();

}

