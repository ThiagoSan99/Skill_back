package com.epw.skillswap.controller;

import com.epw.skillswap.dto.ReviewDTO;
import com.epw.skillswap.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @Valid @RequestBody ReviewDTO dto) {

        return new ResponseEntity<>(
                reviewService.createReview(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reviewService.getReviewById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews(
            @RequestParam(required = false) UUID reviewedUserId,
            @RequestParam(required = false) UUID reviewerUserId) {

        if (reviewedUserId != null) {
            return ResponseEntity.ok(
                    reviewService.getReviewsByReviewedUserId(reviewedUserId)
            );
        }
        if (reviewerUserId != null) {
            return ResponseEntity.ok(
                    reviewService.getReviewsByReviewerUserId(reviewerUserId)
            );
        }

        return ResponseEntity.ok(
                reviewService.getAllReviews()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable UUID id) {

        reviewService.deleteReview(id);

        return ResponseEntity.noContent().build();
    }
}