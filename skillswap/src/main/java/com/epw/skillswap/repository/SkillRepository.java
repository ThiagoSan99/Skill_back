package com.epw.skillswap.repository;

import com.epw.skillswap.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
}