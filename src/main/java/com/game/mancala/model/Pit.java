package com.game.mancala.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pit {
    private String name;
    private Integer stones = null;

    public Pit(String name, int stones) {
        this.name = name;
        this.stones = stones;
    }
}
