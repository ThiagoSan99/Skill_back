package com.epw.skillswap.repository;

import com.epw.skillswap.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserSkillRepository extends JpaRepository<UserSkill, UUID> {

    List<UserSkill> findByUserUserId(UUID userId);
}
