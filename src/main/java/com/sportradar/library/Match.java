package com.sportradar.library;

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

    public Team getHomeTeam() {
        return null;
    }

    public Team getAwayTeam() {
        return null;
    }

    public String getSummary() {

        return "";
    }

}
