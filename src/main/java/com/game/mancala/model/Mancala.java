package com.game.mancala.model;

import lombok.Getter;

@Getter
public class Mancala {
    private Integer numberOfPits;
    private Integer numberOfStones;
    private Player player1;
    private Player player2;

    public Mancala(String firstPlayerUsername, String secondPlayerUsername, int numberOfPits, int numberOfStones) {
        if(numberOfPits == 0) throw new RuntimeException("Pits should be bigger than 0");
        if(numberOfStones == 0) throw new RuntimeException("Stones should be bigger than 0");
        this.numberOfPits = numberOfPits;
        this.numberOfStones = numberOfStones;
        this.player1 = new Player(firstPlayerUsername, numberOfPits, numberOfStones);
        this.player2 = new Player(secondPlayerUsername, numberOfPits, numberOfStones);

    }



}
