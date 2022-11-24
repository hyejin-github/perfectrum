package com.perfectrum.backend.domain.repository;

import com.perfectrum.backend.domain.entity.HaveListEntity;
import com.perfectrum.backend.domain.entity.PerfumeEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.domain.entity.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface HaveListRepository extends JpaRepository<HaveListEntity,Integer> {

    HaveListEntity findByIdx(Integer idx);
    Optional<HaveListEntity> findByUserAndIdx(Optional<UserEntity> userEntityOptional, Integer idx);
    Long countByPerfumeIdxAndIsDelete(Integer perfumeIdx,boolean check);
    Long countByUserAndPerfumeAndIsDelete(UserEntity user,PerfumeEntity perfume,boolean check);
    Optional<HaveListEntity> findByUserAndPerfume(UserEntity user, PerfumeEntity perfume);
    List<HaveListEntity> findByUserAndIsDelete(UserEntity user, boolean b);
}
