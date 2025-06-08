package com.sportradar.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Match match;
    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void init() {
        homeTeam = new Team("TeamA");
        awayTeam = new Team("TeamB");
        match = new Match(homeTeam, awayTeam, 0);
    }

    @Test
    void homeTeamShouldBeSetCorrectly() {
        assertEquals("TeamA", match.getHomeTeam().getName());
    }

    @Test
    void awayTeamShouldBeSetCorrectly() {
        assertEquals("TeamB", match.getAwayTeam().getName());
    }

    @Test
    void homeTeamScoreShouldBeZeroInitially() {
        assertEquals(0, match.getHomeTeam().getScore());
    }

    @Test
    void awayTeamScoreShouldBeZeroInitially() {
        assertEquals(0, match.getAwayTeam().getScore());
    }

    @Test
    void homeTeamScoreShouldUpdateCorrectly() {
        match.updateScore(2, 1);
        assertEquals(2, match.getHomeTeam().getScore());
    }

    @Test
    void awayTeamScoreShouldUpdateCorrectly() {
        match.updateScore(2, 1);
        assertEquals(1, match.getAwayTeam().getScore());
    }

    @Test
    void homeTeamReferenceShouldRemainTheSame() {
        assertSame(homeTeam, match.getHomeTeam());
    }

    @Test
    void awayTeamReferenceShouldRemainTheSame() {
        assertSame(awayTeam, match.getAwayTeam());
    }

    @Test
    void summaryShouldContainCorrectHomeTeamName() {
        MatchSummary summary = match.getSummary();
        assertEquals("TeamA", summary.homeTeamName());
    }

    @Test
    void summaryShouldContainCorrectHomeTeamScore() {
        match.updateScore(2, 3);
        MatchSummary summary = match.getSummary();
        assertEquals(2, summary.homeTeamScore());
    }

    @Test
    void summaryShouldContainCorrectAwayTeamName() {
        MatchSummary summary = match.getSummary();
        assertEquals("TeamB", summary.awayTeamName());
    }

    @Test
    void summaryShouldContainCorrectAwayTeamScore() {
        match.updateScore(2, 3);
        MatchSummary summary = match.getSummary();
        assertEquals(3, summary.awayTeamScore());
    }


}
