package com.epw.skillswap.service;

import com.epw.skillswap.dto.ReportDTO;

import java.util.List;
import java.util.UUID;

public interface ReportService {

    ReportDTO createReport(ReportDTO reportDTO);

    ReportDTO getReportById(UUID reportId);

    List<ReportDTO> getAllReports();

    ReportDTO updateReportStatus(UUID reportId, String status);

    void deleteReport(UUID reportId);
}