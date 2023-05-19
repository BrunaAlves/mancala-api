package com.game.mancala.model;

import com.game.mancala.dto.MancalaGameDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MancalaGame extends Mancala {
    private int currentPlayerIndex;
    private boolean isGameOver;


    public MancalaGame(List<String> playerUsernames, int numberOfPits, int numberOfStones) {
        super(playerUsernames, numberOfPits, numberOfStones);
        this.isGameOver = false;
    }

    public Player getCurrentPlayer() {
        return this.getPlayers().get(this.currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public MancalaGameDTO toEntityDTO(){
        Player currentPlayer = getCurrentPlayer();
        MancalaGameDTO mancalaGameDTO = new MancalaGameDTO();
        mancalaGameDTO.setNumberOfPits(super.getNumberOfPits());
        mancalaGameDTO.setCurrentPlayerId(currentPlayer.getId());
        mancalaGameDTO.setPlayers(this.getPlayers().stream().map(p -> p.toEntityDTO()).collect(Collectors.toList()));
        return mancalaGameDTO;
    }
}
