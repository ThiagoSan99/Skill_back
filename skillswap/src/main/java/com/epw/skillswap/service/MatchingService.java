package com.epw.skillswap.service;

import com.epw.skillswap.dto.MatchingDTO;

import java.util.List;
import java.util.UUID;

public interface MatchingService {

    MatchingDTO createMatching(MatchingDTO dto);

    MatchingDTO getMatchingById(UUID matchId);

    List<MatchingDTO> getAllMatchings();

    MatchingDTO updateMatchingStatus(
            UUID matchId,
            String status
    );

    void deleteMatching(UUID matchId);
}