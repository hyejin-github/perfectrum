package com.perfectrum.backend.domain.repository;

import com.perfectrum.backend.domain.entity.UserDetailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailLogRepository extends JpaRepository<UserDetailLogEntity,Integer> {
}
