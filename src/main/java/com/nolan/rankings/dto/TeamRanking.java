package com.nolan.rankings.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamRanking {
    private String name;
    private Double score;
}