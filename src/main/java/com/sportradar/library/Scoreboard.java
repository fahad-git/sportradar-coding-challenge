package com.sportradar.library;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scoreboard {

    private final List<Match> matches;
    private final AtomicInteger index;

    public Scoreboard() {
        this.matches = new ArrayList<>();
        this.index = new AtomicInteger(0);
    }

    public void startMatch(String homeTeam, String awayTeam) {

    }

    public List<MatchSummary> getOrderedMatches() {
        return List.of();
    }

    public void finishMatch(String homeTeam, String awayTeam) {
    }

    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
    }
}
