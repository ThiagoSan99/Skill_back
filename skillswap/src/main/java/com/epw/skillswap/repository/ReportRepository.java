package com.epw.skillswap.repository;

import com.epw.skillswap.entity.Report;
import com.epw.skillswap.entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findByStatus(ReportStatus status);

    List<Report> findByReporterUserId(UUID reporterUserId);

    List<Report> findByReportedUserId(UUID reportedUserId);
}