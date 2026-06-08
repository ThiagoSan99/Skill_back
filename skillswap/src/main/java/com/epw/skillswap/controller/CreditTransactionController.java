package com.epw.skillswap.controller;

import com.epw.skillswap.dto.CreditTransactionDTO;
import com.epw.skillswap.service.CreditTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class CreditTransactionController {

    private final CreditTransactionService transactionService;

    @PostMapping
    public ResponseEntity<CreditTransactionDTO> createTransaction(
            @Valid @RequestBody CreditTransactionDTO dto) {

        return new ResponseEntity<>(
                transactionService.createTransaction(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditTransactionDTO> getTransactionById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                transactionService.getTransactionById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<CreditTransactionDTO>>
    getAllTransactions() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id) {

        transactionService.deleteTransaction(id);

        return ResponseEntity.noContent().build();
    }
}