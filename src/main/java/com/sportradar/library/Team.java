package com.sportradar.library;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Team {

    private final String name;
    private int score;

    public Team(String name) {
        this.name = name;
        this.score = 0;
    }
}
