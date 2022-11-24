package com.perfectrum.backend.domain.repository;

import com.perfectrum.backend.domain.entity.AccordClassEntity;
import com.perfectrum.backend.domain.entity.UserAccordClassEntity;
import com.perfectrum.backend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccordClassRepository extends JpaRepository<UserAccordClassEntity, Integer> {
    List<UserAccordClassEntity> findByUser(UserEntity user);

    Optional<UserAccordClassEntity> findByUserAndAccordClass(UserEntity user, AccordClassEntity a);
}
