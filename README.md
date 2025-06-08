# Sportradar Live Football World Cup Scoreboard

This project is a **Live Football World Cup Scoreboard Library** that helps manage and display live football matches and their scores. It allows you to start new matches, update scores, finish matches, and get a summary of the ongoing games, sorted by their total scores and start times.

---

##  Features

- **Live Scoreboard**: Track all ongoing matches with up-to-date scores.
- **Start Match**: Create a new match with initial score 0–0.
- **Update Score**: Update the score for any ongoing match.
- **Finish Match**: Mark a match as finished and remove it from the scoreboard.
- **Match Summary**: Retrieve an ordered list of ongoing matches sorted by:
    - Total score (descending)
    - Start time (most recent first)

---

## Getting Started

### Create an Instance

```java
Scoreboard scoreboard = new Scoreboard();
```

---

### Start a New Match

```java
scoreboard.startMatch("Spain", "Brazil");
```

Automatically starts the match with a score of 0-0.

Throws:
- `ScoreboardException` if the team name validation fails, match already in progress, or team already playing.

---

### Update Score

```java
scoreboard.updateScore("Spain", "Brazil", 2, 3);
```

Throws:
- `ScoreboardException` if score validation fails or match is not found.

---

### Finish Match

```java
scoreboard.finishMatch("Spain", "Brazil");
```

Throws:
- `ScoreboardException` if the match does not exist.

---

### Get Summary of Ongoing Matches

```java
List<MatchSummary> matchSummaries = scoreboard.getOrderedMatches();

for (MatchSummary summary : matchSummaries) {
    String line = String.format(
        "%s %d - %s %d",
        summary.homeTeamName(),
        summary.homeTeamScore(),
        summary.awayTeamName(),
        summary.awayTeamScore()
    );
    System.out.println(line);
}

```

Sorted by:
1. Total score (descending)
2. Start order (most recently started match first)

---

##   Assumptions

- Team names are **case-insensitive**.
- A match cannot be started with:
    - The same team on both sides
    - Empty or blank team names
- You cannot start **duplicate matches**.
- Scores must be **non-negative integers**.
- Finished matches are removed from the scoreboard.

---

##   Running Tests

This project uses **JUnit**.

To run the unit tests:

1. **From IntelliJ**: Follow [JetBrains guide](https://www.jetbrains.com/help/idea/work-with-tests-in-maven.html#debug_maven).
2. **Using Maven**:
    ```bash
    mvn test
    ```

All core functionalities are covered under `ScoreboardTest`, including edge cases and error scenarios.

---

##   Suggestions or Feedback

Feel free to open an issue or submit a pull request if you have any feedback or suggestions for improvement.

---

© 2025 Sportradar Candidate Submission
