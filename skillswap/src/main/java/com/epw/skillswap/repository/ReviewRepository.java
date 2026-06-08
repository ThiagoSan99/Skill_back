package com.epw.skillswap.repository;

import com.epw.skillswap.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository
        extends JpaRepository<Review, UUID> {

    List<Review> findByReviewerUserId(UUID reviewerUserId);

    List<Review> findByReviewedUserId(UUID reviewedUserId);

    List<Review> findBySessionSessionId(UUID sessionId);
}