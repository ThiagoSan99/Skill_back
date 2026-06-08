package com.epw.skillswap.service;

import com.epw.skillswap.dto.ReviewDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    ReviewDTO createReview(ReviewDTO dto);

    ReviewDTO getReviewById(UUID reviewId);

    List<ReviewDTO> getAllReviews();

    List<ReviewDTO> getReviewsByReviewedUserId(UUID reviewedUserId);

    List<ReviewDTO> getReviewsByReviewerUserId(UUID reviewerUserId);

    void deleteReview(UUID reviewId);
}