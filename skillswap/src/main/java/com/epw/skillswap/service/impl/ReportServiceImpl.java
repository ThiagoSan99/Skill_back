package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.ReportDTO;
import com.epw.skillswap.entity.*;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.ExchangeSessionRepository;
import com.epw.skillswap.repository.ReportRepository;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.service.ReportService;
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
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ExchangeSessionRepository sessionRepository;

    @Override
    public ReportDTO createReport(ReportDTO dto) {

        User reporter = userRepository.findById(dto.getReporterUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reporter user not found"));

        User reported = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Reported user not found"));

        ExchangeSession session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .session(session)
                .reason(dto.getReason())
                .status(ReportStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToDTO(reportRepository.save(report));
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportById(UUID reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        return mapToDTO(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getAllReports() {

        return reportRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReportDTO updateReportStatus(UUID reportId, String status) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        report.setStatus(ReportStatus.valueOf(status.toUpperCase()));

        return mapToDTO(reportRepository.save(report));
    }

    @Override
    public void deleteReport(UUID reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        reportRepository.delete(report);
    }

    private ReportDTO mapToDTO(Report report) {

        return ReportDTO.builder()
                .reportId(report.getReportId())
                .reporterUserId(report.getReporter().getUserId())
                .reportedUserId(report.getReported().getUserId())
                .sessionId(report.getSession().getSessionId())
                .reason(report.getReason())
                .status(report.getStatus().name())
                .createdAt(report.getCreatedAt())
                .build();
    }
}