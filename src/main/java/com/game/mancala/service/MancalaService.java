package com.game.mancala.service;

import com.game.mancala.model.MancalaGame;
import com.game.mancala.model.Pit;
import com.game.mancala.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class MancalaService {

    static MancalaGame mancala = null;

    public MancalaGame get(){
        return mancala;
    }

    public MancalaGame startGame() {
        if(mancala != null ) throw new RuntimeException("A game is already running");
        mancala = new MancalaGame(List.of("player1", "player2"),6,6);

        Random random = new Random();
        setTurn(random.nextInt(mancala.getPlayers().size()));
        return mancala;
    }

    public MancalaGame getMoves(UUID id){
        Pit startPit = mancala.getCurrentPlayer()
                .getPits()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("There is no pit with uuid %s", id)));

        int pitIndex =  mancala.getCurrentPlayer()
                .getPits().indexOf(startPit);

        int handStones = emptyPit(startPit);
        int playerIndex = mancala.getCurrentPlayerIndex();

        while (handStones > 0){
            pitIndex ++;

            if(pitIndex >= mancala.getPlayers().get(playerIndex).getPits().size()) {
                pitIndex = 0;
                playerIndex ++;
                if(playerIndex >= mancala.getPlayers().size()) {
                    playerIndex = 0;
                }
            }
            Pit currentPit = mancala.getPlayers().get(playerIndex).getPits().get(pitIndex);

            if(currentPit.equals(mancala.getPlayers().get(playerIndex).getLargePit())) {
                if(currentPit.equals(mancala.getCurrentPlayer().getLargePit())) {
                    currentPit.setStones(currentPit.getStones() + 1);
                    handStones --;
                }
            } else {
                currentPit.setStones(currentPit.getStones() + 1);
                handStones --;
            }


        }

        Player winner = getWinner();
        System.out.println(winner);

        if(!mancala.getPlayers().get(playerIndex).equals(mancala.getCurrentPlayer())) {
            toggleTurn();
        }

        return mancala;
    }

    public Player getWinner() {
        for (Player player : mancala.getPlayers()) {
            int sum = player.getPits().stream().filter(x -> !x.equals(player.getLargePit())).mapToInt(x -> x.getStones()).sum();

            if(sum == 0) {
                return player;
            }
        }
        return null;
    }

    public void endGame() {
        if(mancala == null) throw new RuntimeException("There is no game started");
        mancala = null;
    }

    private void setTurn(int index) {
        mancala.setCurrentPlayerIndex(index);
    }

    private void toggleTurn() {
        int nextIndex = mancala.getCurrentPlayerIndex();
        nextIndex ++;
        if(nextIndex >= mancala.getPlayers().size()) {
            nextIndex = 0;
        }
        setTurn(nextIndex);
    }

    private int emptyPit(Pit pit){
       int handStones = pit.getStones();
       pit.setStones(0);
       return handStones;
    }
}
