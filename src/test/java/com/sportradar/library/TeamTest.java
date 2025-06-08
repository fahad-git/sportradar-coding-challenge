package com.sportradar.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team team;

    @BeforeEach
    void init() {
        team = new Team("TeamA");
    }

    @Test
    void teamNameShouldBeSetOnCreation() {
        assertEquals("TeamA", team.getName());
    }

    @Test
    void teamScoreShouldBeZeroOnCreation() {
        assertEquals(0, team.getScore());
    }

    @Test
    void teamScoreShouldBeUpdatable() {
        team.setScore(7);
        assertEquals(7, team.getScore());
    }

    @Test
    void teamNameShouldBeImmutable() {
        assertEquals("TeamA", team.getName());
    }
}
