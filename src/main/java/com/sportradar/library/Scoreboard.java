package com.sportradar.library;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Scoreboard {

    private final ReentrantLock lock;
    private final List<Match> matches;
    private final AtomicInteger index;

    public Scoreboard() {
        this.lock = new ReentrantLock();
        this.matches = new ArrayList<>();
        this.index = new AtomicInteger(0);
    }

    public void startMatch(String homeTeamName, String awayTeamName) throws ScoreboardException {
        lock.lock();
        try {
            validateTeamNames(homeTeamName, awayTeamName);

            if (findMatch(homeTeamName, awayTeamName).isPresent()) {
                throw new ScoreboardException(
                        "It is not possible to have two same matches at the same time."
                );
            }

            if (isTeamPlaying(homeTeamName) || isTeamPlaying(awayTeamName)) {
                throw new ScoreboardException("A team is already playing in another match.");
            }

            Match match = new Match(new Team(homeTeamName), new Team(awayTeamName), this.index.incrementAndGet());
            matches.add(match);
        } finally {
            lock.unlock();
        }
    }

    public void finishMatch(String homeTeamName, String awayTeamName) throws ScoreboardException {
        lock.lock();
        try {
            validateTeamNames(homeTeamName, awayTeamName);

            boolean removed = matches.removeIf(match ->
                    match.getHomeTeam().getName().equalsIgnoreCase(homeTeamName) &&
                            match.getAwayTeam().getName().equalsIgnoreCase(awayTeamName)
            );

            if (!removed) {
                throw new ScoreboardException("Cannot finish match that doesn't exist.");
            }
        } finally {
            lock.unlock();
        }
    }

    public void updateScore(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore) throws ScoreboardException {
        lock.lock();
        try {
            validateTeamScores(homeTeamScore, awayTeamScore);

            Optional<Match> optionalMatch = findMatch(homeTeamName, awayTeamName);

            if (optionalMatch.isEmpty()) {
                throw new ScoreboardException("It is not possible to update match which is not active.");
            }

            Match foundMatch = optionalMatch.get();
            foundMatch.updateScore(homeTeamScore, awayTeamScore);
        } finally {
            lock.unlock();
        }
    }

    public List<MatchSummary> getOrderedMatches() {
        lock.lock();
        try {
            return matches.stream()
                    .sorted(Comparator.comparing(this::getTotalScore, Comparator.reverseOrder())
                            .thenComparing(Match::getIndex, Comparator.reverseOrder()))
                    .map(Match::getSummary)
                    .toList();
        } finally {
            lock.unlock();
        }
    }

    private Optional<Match> findMatch(String homeTeamName, String awayTeamName) {
        return matches.stream()
                .filter(m -> m.getHomeTeam().getName().equalsIgnoreCase(homeTeamName))
                .filter(m -> m.getAwayTeam().getName().equalsIgnoreCase(awayTeamName))
                .findFirst();
    }

    private boolean isTeamPlaying(String teamName) {
        return matches.stream()
                .anyMatch(m -> m.getHomeTeam().getName().equalsIgnoreCase(teamName)
                        || m.getAwayTeam().getName().equalsIgnoreCase(teamName));
    }

    private int getTotalScore(Match match) {
        return match.getHomeTeam().getScore() + match.getAwayTeam().getScore();
    }

    private void validateTeamScores(int homeTeamScore, int awayTeamScore) {
        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new IllegalArgumentException("It is not possible to update match with negative values.");
        }
    }

    private void validateTeamNames(String homeTeam, String awayTeam) throws ScoreboardException {
        if (homeTeam == null || awayTeam == null || homeTeam.isBlank() || awayTeam.isBlank()) {
            throw new ScoreboardException("Team name cannot be empty.");
        }
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new ScoreboardException("Teams cannot have the same name.");
        }
    }
}