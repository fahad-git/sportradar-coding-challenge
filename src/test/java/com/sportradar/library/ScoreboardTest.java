package com.sportradar.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

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

    @Test
    void shouldReturnFirstMatcheOrderedByTotalScore() throws ScoreboardException {
        createMatchesInOrder();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        String result = String.format("%s %d - %s %d",
                matches.get(0).homeTeamName(), matches.get(0).homeTeamScore(),
                matches.get(0).awayTeamName(), matches.get(0).awayTeamScore()
        );
        assertEquals("Uruguay 6 - Italy 6", result);
    }

    @Test
    void shouldReturnSecondMatcheOrderedByTotalScore() throws ScoreboardException {
        createMatchesInOrder();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        String result = String.format("%s %d - %s %d",
                matches.get(1).homeTeamName(), matches.get(1).homeTeamScore(),
                matches.get(1).awayTeamName(), matches.get(1).awayTeamScore()
        );
        assertEquals("Spain 10 - Brazil 2", result);
    }

    @Test
    void shouldReturnThirdMatcheOrderedByTotalScore() throws ScoreboardException {
        createMatchesInOrder();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        String result = String.format("%s %d - %s %d",
                matches.get(2).homeTeamName(), matches.get(2).homeTeamScore(),
                matches.get(2).awayTeamName(), matches.get(2).awayTeamScore()
        );
        assertEquals("Mexico 0 - Canada 5", result);
    }

    @Test
    void shouldReturnFourthMatcheOrderedByTotalScore() throws ScoreboardException {
        createMatchesInOrder();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        String result = String.format("%s %d - %s %d",
                matches.get(3).homeTeamName(), matches.get(3).homeTeamScore(),
                matches.get(3).awayTeamName(), matches.get(3).awayTeamScore()
        );
        assertEquals("Argentina 3 - Australia 1", result);
    }

    @Test
    void shouldReturnFifthMatcheOrderedByTotalScore() throws ScoreboardException {
        createMatchesInOrder();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        String result = String.format("%s %d - %s %d",
                matches.get(4).homeTeamName(), matches.get(4).homeTeamScore(),
                matches.get(4).awayTeamName(), matches.get(4).awayTeamScore()
        );
        assertEquals("Germany 2 - France 2", result);
    }
    @Test
    void concurrentStartAndUpdate_ShouldOnlyCreateAtMostOneMatch() throws InterruptedException {
        int threads = 10;
        CountDownLatch latch = new CountDownLatch(1);

        List<Thread> workers = IntStream.range(0, threads)
                .mapToObj(i -> new Thread(() -> {
                    try {
                        latch.await();
                        if (i % 2 == 0) {
                            scoreboard.startMatch("TeamX", "TeamY");
                        } else {
                            scoreboard.updateScore("TeamX", "TeamY", 1, 1);
                        }
                    } catch (Exception ignored) { }
                }))
                .toList();

        workers.forEach(Thread::start);
        latch.countDown();
        for (Thread t : workers) t.join();

        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        assertTrue(matches.size() <= 1);
    }

    @Test
    void concurrentStartAndUpdate_HomeScoreShouldBeNonNegative() throws InterruptedException {
        runConcurrentStartAndUpdate();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        if (!matches.isEmpty()) {
            assertTrue(matches.get(0).homeTeamScore() >= 0);
        }
    }

    @Test
    void concurrentStartAndUpdate_AwayScoreShouldBeNonNegative() throws InterruptedException {
        runConcurrentStartAndUpdate();
        List<MatchSummary> matches = scoreboard.getOrderedMatches();
        if (!matches.isEmpty()) {
            assertTrue(matches.get(0).awayTeamScore() >= 0);
        }
    }

    //helper function for inserting data into the list
    private void createMatchesInOrder() throws ScoreboardException {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);

        scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore("Argentina", "Australia", 3, 1);
    }

    //helper function for starting new threads
    private void runConcurrentStartAndUpdate() throws InterruptedException {
        int threads = 10;
        CountDownLatch latch = new CountDownLatch(1);

        List<Thread> workers = IntStream.range(0, threads)
                .mapToObj(i -> new Thread(() -> {
                    try {
                        latch.await();
                        if (i % 2 == 0) {
                            scoreboard.startMatch("TeamX", "TeamY");
                        } else {
                            scoreboard.updateScore("TeamX", "TeamY", 1, 1);
                        }
                    } catch (Exception ignored) {
                    }
                }))
                .toList();

        workers.forEach(Thread::start);
        latch.countDown();
        for (Thread t : workers) t.join();
    }

}
