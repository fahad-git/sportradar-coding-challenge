package com.sportradar.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    private Scoreboard scoreboard;

    @BeforeEach
    void init() {
        scoreboard = new Scoreboard();
    }

    @Test
    void matchShouldBeStartedWithGivenTeams() throws ScoreboardException {
        scoreboard.startMatch("TeamA", "TeamB");
        MatchSummary match = scoreboard.getOrderedMatches().get(0);
        assertEquals("TeamA", match.homeTeamName());
    }

    @Test
    void awayTeamShouldBeSetCorrectly() throws ScoreboardException {
        scoreboard.startMatch("TeamA", "TeamB");
        MatchSummary match = scoreboard.getOrderedMatches().get(0);
        assertEquals("TeamB", match.awayTeamName());
    }

    @Test
    void shouldThrowIfMatchIsDuplicated() throws ScoreboardException {
        scoreboard.startMatch("TeamA", "TeamB");
        assertThrows(ScoreboardException.class, () -> scoreboard.startMatch("TeamA", "TeamB"));
    }

    @Test
    void shouldThrowIfTeamNamesAreInvalid() {
        assertThrows(ScoreboardException.class, () -> scoreboard.startMatch("TeamA", "TeamA"));
    }

    @Test
    void shouldThrowIfTeamNamesAreBlank() {
        assertThrows(ScoreboardException.class, () -> scoreboard.startMatch(" ", "TeamB"));
    }

    @Test
    void shouldThrowIfTeamNameIsNull() {
        assertThrows(ScoreboardException.class, () -> scoreboard.startMatch("TeamA", null));
    }

    @Test
    void matchShouldBeFinishedAndRemoved() throws ScoreboardException {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.finishMatch("TeamA", "TeamB");
        assertEquals(0, scoreboard.getOrderedMatches().size());
    }

    @Test
    void shouldThrowWhenFinishingNonExistingMatch() {
        assertThrows(ScoreboardException.class, () -> scoreboard.finishMatch("TeamX", "TeamY"));
    }

    @Test
    void shouldUpdateScoreForHomeTeam() throws Exception {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.updateScore("TeamA", "TeamB", 2, 0);
        MatchSummary match = scoreboard.getOrderedMatches().get(0);
        assertEquals(2, match.homeTeamScore());
    }

    @Test
    void shouldUpdateScoreForAwayTeam() throws Exception {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.updateScore("TeamA", "TeamB", 0, 3);
        MatchSummary match = scoreboard.getOrderedMatches().get(0);
        assertEquals(3, match.awayTeamScore());
    }

    @Test
    void shouldThrowForNegativeHomeScore() throws Exception {
        scoreboard.startMatch("TeamA", "TeamB");
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore("TeamA", "TeamB", -1, 2));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingMatch() {
        assertThrows(ScoreboardException.class, () -> scoreboard.updateScore("TeamX", "TeamY", 1, 1));
    }

    @Test
    void matchesShouldBeSortedByTotalScoreAndRecency_HomeTeamName() throws Exception {
        scoreboard.startMatch("Team1", "Team2");
        scoreboard.startMatch("Team3", "Team4");

        scoreboard.updateScore("Team1", "Team2", 2, 3);
        scoreboard.updateScore("Team3", "Team4", 2, 3);

        List<MatchSummary> matches = scoreboard.getOrderedMatches();

        assertEquals("Team3", matches.get(0).homeTeamName());
    }

    @Test
    void matchWithHigherScoreShouldBeFirst_HomeTeamName() throws Exception {
        scoreboard.startMatch("TeamA", "TeamB");
        scoreboard.startMatch("TeamC", "TeamD");

        scoreboard.updateScore("TeamA", "TeamB", 1, 1);
        scoreboard.updateScore("TeamC", "TeamD", 3, 0);

        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        assertEquals("TeamC", matches.get(0).homeTeamName());
    }

    @Test
    void shouldThrowIfTeamAlreadyPlaying_HomeTeamPlaying() throws ScoreboardException {
        scoreboard.startMatch("TeamA", "TeamB");
        assertThrows(ScoreboardException.class, () -> scoreboard.startMatch("TeamA", "TeamC"));
    }

    @Test
    void shouldThrowIfTeamAlreadyPlaying_AwayTeamPlaying() throws ScoreboardException {
        scoreboard.startMatch("TeamA", "TeamB");
        assertThrows(ScoreboardException.class, () -> scoreboard.startMatch("TeamD", "TeamB"));
    }
}
