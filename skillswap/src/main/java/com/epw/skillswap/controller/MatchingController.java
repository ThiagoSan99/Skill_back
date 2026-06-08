package com.epw.skillswap.controller;

import com.epw.skillswap.dto.MatchingDTO;
import com.epw.skillswap.service.MatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/matchings")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<MatchingDTO> createMatching(
            @Valid @RequestBody MatchingDTO dto) {

        return new ResponseEntity<>(
                matchingService.createMatching(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchingDTO> getMatchingById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                matchingService.getMatchingById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<MatchingDTO>>
    getAllMatchings() {

        return ResponseEntity.ok(
                matchingService.getAllMatchings()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MatchingDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                matchingService.updateMatchingStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatching(
            @PathVariable UUID id) {

        matchingService.deleteMatching(id);

        return ResponseEntity.noContent().build();
    }
}