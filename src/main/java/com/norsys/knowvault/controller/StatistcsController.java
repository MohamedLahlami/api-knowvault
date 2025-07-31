package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.DashboardDTO;
import com.norsys.knowvault.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatistcsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public DashboardDTO getDashboard() {
        return statisticsService.getDashboardStatistics();
    }
}
