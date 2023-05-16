package com.game.mancala.model;

public class MancalaGame extends Mancala{
    private Player currentPlayer;

    public MancalaGame(String firstPlayerUsername, String secondPlayerUsername, int numberOfPits, int numberOfStones) {
        super(firstPlayerUsername, secondPlayerUsername, numberOfPits, numberOfStones);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
