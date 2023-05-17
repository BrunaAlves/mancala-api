package com.game.mancala.model;

import com.game.mancala.dto.MancalaGameDTO;

public class MancalaGame extends Mancala {
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

    public MancalaGameDTO toEntityDTO(){
        MancalaGameDTO mancalaGameDTO = new MancalaGameDTO();
        mancalaGameDTO.setNumberOfPits(super.getNumberOfPits());
        mancalaGameDTO.setCurrentPlayerId(this.currentPlayer.getId());
        mancalaGameDTO.setPlayer1(super.getPlayer1().toEntityDTO());
        mancalaGameDTO.setPlayer2(super.getPlayer2().toEntityDTO());
        return mancalaGameDTO;
    }
}
