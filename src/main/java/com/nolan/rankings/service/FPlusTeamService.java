package com.nolan.rankings.service;

import com.nolan.rankings.dto.TeamRanking;
import com.nolan.rankings.model.FPlusTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FPlusTeamService {
    private final ReactiveRedisTemplate<String, FPlusTeam> redisTemplate;
    private static final String KEY_PREFIX = "fplus_team:";

    public Mono<FPlusTeam> save(FPlusTeam team) {
        return redisTemplate.opsForValue()
            .set(KEY_PREFIX + team.getRank(), team)
            .thenReturn(team);
    }

    public Flux<FPlusTeam> saveAll(Flux<FPlusTeam> teams) {
        return teams.flatMap(this::save);
    }

    public Flux<FPlusTeam> findAll() {
        return redisTemplate.keys(KEY_PREFIX + "*")
            .flatMap(key -> redisTemplate.opsForValue().get(key));
    }

    public Flux<TeamRanking> findAllSorted() {
        return findAll().map(fpteam -> {
            return TeamRanking.builder()
                .name(fpteam.getName())
                .score(fpteam.getOffense()*.6 + fpteam.getDefense()*.4)
                .build();
        }).sort((a, b) -> a.getScore().compareTo(b.getScore()));
    }
}