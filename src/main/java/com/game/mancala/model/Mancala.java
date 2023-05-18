package com.game.mancala.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Mancala {
    private Integer numberOfPits;
    private Integer numberOfStones;
    private List<Player> players;

    public Mancala(List<String> playersUsername, int numberOfPits, int numberOfStones) {
        if(numberOfPits == 0) throw new RuntimeException("Pits should be bigger than 0");
        if(numberOfStones == 0) throw new RuntimeException("Stones should be bigger than 0");
        this.numberOfPits = numberOfPits;
        this.numberOfStones = numberOfStones;

        this.players = new ArrayList<>();

        for (String username : playersUsername) {
            this.players.add(new Player(username, numberOfPits, numberOfStones));
        }

    }



}
