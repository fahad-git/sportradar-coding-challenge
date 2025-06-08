package com.sportradar.library;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Match {

    private final Team homeTeam;

    private final Team awayTeam;

    private final int index;

    public Match(Team homeTeam, Team awayTeam, int index) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.index = index;
    }

    public void updateScore(int homeScore, int awayScore) {
        getTeam().setScore(homeScore);
        awayTeam.setScore(awayScore);
    }

    private Team getTeam() {
        return homeTeam;
    }

    public MatchSummary getSummary() {
        return new MatchSummary(
                this.getHomeTeam().getName(),
                this.getHomeTeam().getScore(),
                this.getAwayTeam().getName(),
                this.getAwayTeam().getScore());
    }
}
