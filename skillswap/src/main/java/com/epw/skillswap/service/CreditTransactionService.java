package com.epw.skillswap.service;

import com.epw.skillswap.dto.CreditTransactionDTO;

import java.util.List;
import java.util.UUID;

public interface CreditTransactionService {

    CreditTransactionDTO createTransaction(
            CreditTransactionDTO dto);

    CreditTransactionDTO getTransactionById(
            UUID transactionId);

    List<CreditTransactionDTO> getAllTransactions();

    void deleteTransaction(UUID transactionId);
}