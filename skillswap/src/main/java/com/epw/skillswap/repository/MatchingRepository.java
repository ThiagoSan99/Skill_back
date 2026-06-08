package com.epw.skillswap.repository;

import com.epw.skillswap.entity.Matching;
import com.epw.skillswap.entity.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchingRepository
        extends JpaRepository<Matching, UUID> {

    List<Matching> findByRequesterUserUserId(
            UUID requesterUserId);

    List<Matching> findByMatchedUserUserId(
            UUID matchedUserId);

    List<Matching> findByStatus(
            MatchingStatus status);
}