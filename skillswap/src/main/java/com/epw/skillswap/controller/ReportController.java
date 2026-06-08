package com.epw.skillswap.controller;

import com.epw.skillswap.dto.ReportDTO;
import com.epw.skillswap.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportDTO> createReport(
            @Valid @RequestBody ReportDTO reportDTO) {

        return new ResponseEntity<>(
                reportService.createReport(reportDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reportService.getReportById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {

        return ResponseEntity.ok(
                reportService.getAllReports()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                reportService.updateReportStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @PathVariable UUID id) {

        reportService.deleteReport(id);

        return ResponseEntity.noContent().build();
    }
}