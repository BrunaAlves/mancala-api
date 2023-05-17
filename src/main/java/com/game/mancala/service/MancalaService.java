package com.game.mancala.service;

import com.game.mancala.model.MancalaGame;
import com.game.mancala.model.Player;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MancalaService {

    static MancalaGame mancala = null;

    public MancalaGame get(){
        return mancala;
    }

    public MancalaGame startGame() {
        if(mancala != null ) throw new RuntimeException("A game is already running");
        mancala = new MancalaGame("player1", "player2",6,6);

        Random random = new Random();
        setTurn(random.nextInt(2) == 0 ? mancala.getPlayer1() : mancala.getPlayer2());
        return mancala;
    }

    public void endGame() {
        if(mancala == null) throw new RuntimeException("There is no game started");
        mancala = null;
    }

    private void setTurn(Player player) {

        mancala.setCurrentPlayer(player);
    }

    private void toggleTurn() {
        setTurn(mancala.getCurrentPlayer().equals(mancala.getPlayer1()) ? mancala.getPlayer2() : mancala.getPlayer1());
    }
}
