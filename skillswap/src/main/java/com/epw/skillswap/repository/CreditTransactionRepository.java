package com.epw.skillswap.repository;

import com.epw.skillswap.entity.CreditTransaction;
import com.epw.skillswap.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CreditTransactionRepository
        extends JpaRepository<CreditTransaction, UUID> {

    List<CreditTransaction> findBySenderUserId(UUID senderUserId);

    List<CreditTransaction> findByReceiverUserId(UUID receiverUserId);

    List<CreditTransaction> findByTransactionType(
            TransactionType transactionType);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM CreditTransaction t " +
           "WHERE t.receiver.userId = :userId " +
           "AND t.transactionDate BETWEEN :start AND :end " +
           "AND t.transactionType = 'EARNED'")
    Double sumEarnedByReceiverBetween(@Param("userId") UUID userId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM CreditTransaction t " +
           "WHERE t.sender.userId = :userId " +
           "AND t.transactionDate BETWEEN :start AND :end " +
           "AND t.transactionType = 'SPENT'")
    Double sumSpentBySenderBetween(@Param("userId") UUID userId,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    @Query("SELECT t FROM CreditTransaction t " +
           "WHERE (t.sender.userId = :userId OR t.receiver.userId = :userId) " +
           "AND t.transactionDate >= :since " +
           "ORDER BY t.transactionDate ASC")
    List<CreditTransaction> findByUserSince(@Param("userId") UUID userId,
                                             @Param("since") LocalDateTime since);
}