package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.MatchingDTO;
import com.epw.skillswap.entity.*;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.MatchingRepository;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl
        implements MatchingService {

    private final MatchingRepository matchingRepository;
    private final UserRepository userRepository;

    @Override
    public MatchingDTO createMatching(MatchingDTO dto) {

        User requester = userRepository.findById(
                dto.getRequesterUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Requester user not found"
                ));

        User matched = userRepository.findById(
                dto.getMatchedUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Matched user not found"
                ));

        Matching matching = Matching.builder()
                .requesterUser(requester)
                .matchedUser(matched)
                .matchingScore(dto.getMatchingScore())
                .matchingReason(dto.getMatchingReason())
                .status(MatchingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToDTO(
                matchingRepository.save(matching)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MatchingDTO getMatchingById(UUID matchId) {

        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Matching not found"
                        ));

        return mapToDTO(matching);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchingDTO> getAllMatchings() {

        return matchingRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MatchingDTO updateMatchingStatus(
            UUID matchId,
            String status) {

        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Matching not found"
                        ));

        matching.setStatus(
                MatchingStatus.valueOf(
                        status.toUpperCase()
                )
        );

        return mapToDTO(
                matchingRepository.save(matching)
        );
    }

    @Override
    public void deleteMatching(UUID matchId) {

        Matching matching = matchingRepository.findById(matchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Matching not found"
                        ));

        matchingRepository.delete(matching);
    }

    private MatchingDTO mapToDTO(Matching matching) {

        return MatchingDTO.builder()
                .matchId(matching.getMatchId())
                .requesterUserId(
                        matching.getRequesterUser().getUserId()
                )
                .matchedUserId(
                        matching.getMatchedUser().getUserId()
                )
                .matchingScore(matching.getMatchingScore())
                .matchingReason(matching.getMatchingReason())
                .status(matching.getStatus().name())
                .createdAt(matching.getCreatedAt())
                .build();
    }
}