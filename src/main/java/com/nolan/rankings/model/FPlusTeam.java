package com.nolan.rankings.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FPlusTeam {
    private Integer rank;
    private String name;
    private Double offense;
    private Double defense;
    private Double overall;
    private String lastUpdated;
}