package com.game.mancala.model;

import com.game.mancala.exception.MancalaException;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Mancala {
    private Integer numberOfPits;
    private Integer numberOfStones;
    private List<Player> players;

    public Mancala(List<String> playersUsername, int numberOfPits, int numberOfStones) {
        if(numberOfPits <= 0) throw new MancalaException("Pits should be bigger than 0");
        if(numberOfStones <= 0) throw new MancalaException("Stones should be bigger than 0");
        if(numberOfPits >= 13) throw new MancalaException("Pits should be less than 13");
        if(numberOfStones >= 13) throw new MancalaException("Stones should be less than 13");
        if(playersUsername == null) throw new MancalaException("PlayersUsername cannot be null");
        if(playersUsername.size() < 2) throw new MancalaException("PlayersUsername should be more than 2");
        if(playersUsername.size() >= 11) throw new MancalaException("PlayersUsername should be less than 11");

        this.numberOfPits = numberOfPits;
        this.numberOfStones = numberOfStones;

        this.players = new ArrayList<>();

        for (String username : playersUsername) {
            this.players.add(new Player(username, numberOfPits, numberOfStones));
        }

    }



}
