package com.nolan.rankings.service;

import com.nolan.rankings.model.FPlusTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapingService {
    private final WebClient.Builder webClientBuilder;
    private final FPlusTeamService teamService;
    private static final String TARGET_URL = "https://bcftoys.com/2025-fplus";

    public Mono<Void> scrapeAndSaveData() {
        return webClientBuilder.build()
                .get()
                .uri(TARGET_URL)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseHtml)
                .flatMapMany(Flux::fromIterable)
                .transform(teamService::saveAll)
                .then();
    }

    private List<FPlusTeam> parseHtml(String html) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("table tr");
        List<FPlusTeam> teams = new ArrayList<>();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        for (Element row : rows) {
            Elements cols = row.select("td");
            String name = cols.get(1).text().trim();
            if ("Team".equals(name)) {
                continue;
            }
            FPlusTeam team = FPlusTeam.builder()
                    .rank(Integer.parseInt(cols.get(0).text().trim()))
                    .name(name)
                    .offense(parseDouble(cols.get(5).text()))
                    .defense(parseDouble(cols.get(7).text()))
                    .overall(parseDouble(cols.get(4).text()))
                    .lastUpdated(timestamp)
                    .build();
            teams.add(team);
        }
        return teams;
    }

    private Double parseDouble(String value) {
        return Double.parseDouble(value.trim());
    }
}