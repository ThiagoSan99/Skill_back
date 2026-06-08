package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.ReviewDTO;
import com.epw.skillswap.entity.ExchangeSession;
import com.epw.skillswap.entity.Review;
import com.epw.skillswap.entity.SessionStatus;
import com.epw.skillswap.entity.User;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.ExchangeSessionRepository;
import com.epw.skillswap.repository.ReviewRepository;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.service.ReviewService;
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
public class ReviewServiceImpl
        implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ExchangeSessionRepository sessionRepository;

    @Override
    public ReviewDTO createReview(ReviewDTO dto) {

        User reviewer = userRepository.findById(
                dto.getReviewerUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Reviewer not found"
                ));

        User reviewed = userRepository.findById(
                dto.getReviewedUserId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Reviewed user not found"
                ));

        ExchangeSession session = sessionRepository.findById(
                dto.getSessionId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Session not found"
                ));

        if (session.getStatus() != SessionStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Review can only be created for completed sessions"
            );
        }

        Review review = Review.builder()
                .reviewer(reviewer)
                .reviewed(reviewed)
                .session(session)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        updateUserReputation(reviewed, dto.getRating());

        return mapToDTO(
                reviewRepository.save(review)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(UUID reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found"
                        ));

        return mapToDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviews() {

        return reviewRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByReviewedUserId(UUID reviewedUserId) {
        return reviewRepository.findByReviewedUserId(reviewedUserId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByReviewerUserId(UUID reviewerUserId) {
        return reviewRepository.findByReviewerUserId(reviewerUserId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReview(UUID reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found"
                        ));

        reviewRepository.delete(review);
    }

    private void updateUserReputation(
            User user,
            Integer newRating) {

        Double currentScore =
                user.getReputationScore() == null
                        ? 0.0
                        : user.getReputationScore();

        Double updatedScore =
                (currentScore + newRating) / 2;

        user.setReputationScore(updatedScore);
    }

    private ReviewDTO mapToDTO(Review review) {

        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .sessionId(review.getSession().getSessionId())
                .reviewerUserId(review.getReviewer().getUserId())
                .reviewedUserId(review.getReviewed().getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}