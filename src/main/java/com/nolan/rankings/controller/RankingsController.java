package com.nolan.rankings.controller;

import com.nolan.rankings.dto.TeamRanking;
import com.nolan.rankings.service.FPlusTeamService;
import com.nolan.rankings.service.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingsController {
    private final ScrapingService scrapingService;
    private final FPlusTeamService teamService;

    @GetMapping("/scrape")
    public Mono<ResponseEntity<String>> scrapeData() {
        return scrapingService.scrapeAndSaveData()
                .then(Mono.just(ResponseEntity.ok("Data scraped and saved successfully")));
    }

    @GetMapping
    public Flux<TeamRanking> getAllRankings() {
        return teamService.findAllSorted();
    }
}