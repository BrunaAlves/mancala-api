package com.game.mancala.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Player {
    private UUID id;
    private String username;
    private List<Pit> pits = null;

    public Player(String username, int numberOfPits, int numberOfStones) {
        this.username = username;
        this.id = UUID.randomUUID();
        this.pits = new ArrayList<>();

        for (int i = 0; i < numberOfPits; i++) {
            this.pits.add(new Pit(this.username + ":" + i, numberOfStones));

        }
    }
}
