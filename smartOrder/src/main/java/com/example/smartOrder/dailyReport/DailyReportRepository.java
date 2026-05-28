package com.example.smartOrder.dailyReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyReportRepository extends JpaRepository<DailyReport, Integer> {

    Optional<DailyReport> findByReportDate(LocalDate reportDate);
}