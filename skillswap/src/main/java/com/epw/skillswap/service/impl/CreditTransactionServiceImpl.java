package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.CreditTransactionDTO;
import com.epw.skillswap.entity.*;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.CreditTransactionRepository;
import com.epw.skillswap.repository.ExchangeSessionRepository;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.service.CreditTransactionService;
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
public class CreditTransactionServiceImpl
        implements CreditTransactionService {

    private final CreditTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ExchangeSessionRepository sessionRepository;

    @Override
    public CreditTransactionDTO createTransaction(
            CreditTransactionDTO dto) {

        User sender = userRepository.findById(dto.getSenderUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sender not found"));

        User receiver = userRepository.findById(dto.getReceiverUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Receiver not found"));

        ExchangeSession session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Session not found"));

        CreditTransaction transaction = CreditTransaction.builder()
                .sender(sender)
                .receiver(receiver)
                .session(session)
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .transactionType(
                        TransactionType.valueOf(
                                dto.getTransactionType().toUpperCase()
                        )
                )
                .build();

        sender.setCurrentCreditBalance(
                sender.getCurrentCreditBalance() - dto.getAmount()
        );

        receiver.setCurrentCreditBalance(
                receiver.getCurrentCreditBalance() + dto.getAmount()
        );

        return mapToDTO(
                transactionRepository.save(transaction)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CreditTransactionDTO getTransactionById(
            UUID transactionId) {

        CreditTransaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                ));

        return mapToDTO(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditTransactionDTO> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransaction(UUID transactionId) {

        CreditTransaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found"
                                ));

        transactionRepository.delete(transaction);
    }

    private CreditTransactionDTO mapToDTO(
            CreditTransaction transaction) {

        return CreditTransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .sessionId(transaction.getSession().getSessionId())
                .senderUserId(transaction.getSender().getUserId())
                .receiverUserId(transaction.getReceiver().getUserId())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(
                        transaction.getTransactionType().name()
                )
                .build();
    }
}