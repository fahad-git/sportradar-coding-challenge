package com.sportradar.library;

public record MatchSummary(
        String homeTeamName,
        int homeTeamScore,
        String awayTeamName,
        int awayTeamScore
) {}
