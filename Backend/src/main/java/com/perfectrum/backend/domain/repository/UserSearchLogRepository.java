package com.perfectrum.backend.domain.repository;

import com.perfectrum.backend.domain.entity.UserSearchLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSearchLogRepository extends JpaRepository<UserSearchLogEntity, Integer> {

}