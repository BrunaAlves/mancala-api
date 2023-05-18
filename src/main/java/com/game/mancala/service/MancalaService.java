package com.game.mancala.service;

import com.game.mancala.dto.GameActionsDTO;
import com.game.mancala.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MancalaService {

    private static MancalaGame mancala = null;
    private static List<Action> gameActions = null;

    public MancalaGame get(){
        return mancala;
    }

    public MancalaGame startGame() {
        if(mancala != null ) throw new RuntimeException("A game is already running");
        this.mancala = new MancalaGame(List.of("player1", "player2"),6,6);
        this.gameActions = new ArrayList<>();

        Random random = new Random();
        setTurn(random.nextInt(mancala.getPlayers().size()));
        return mancala;
    }

    public GameActionsDTO getActions(UUID id){
        this.movePits(id);
        return new GameActionsDTO(gameActions, mancala.toEntityDTO());
    }

    //TODO: improve function
    private void movePits(UUID id){
        Pit startPit = getStartPit(id);

        if(startPit.getStones() == 0) {
            throw new RuntimeException(String.format("There is no stones on pit %s",startPit.getName()));
        }

        int pitIndex =  mancala.getCurrentPlayer().getPits().indexOf(startPit);
        int handStones = emptyPit(startPit);
        int playerIndex = mancala.getCurrentPlayerIndex();
        Pit lastPitChecked = startPit;

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

                if(handStones == 0 && currentPit.getStones() == 1) {
                    capturesOwnAndOppositeStones(currentPit);
                }
            }

            this.gameActions.add(new MoveAction(lastPitChecked.getId(), currentPit.getId(),1, handStones));
            lastPitChecked = currentPit;
        }

        Player winner = getWinner();
        System.out.println(winner);

        if(findCurrentPlayerPitById(lastPitChecked.getId()).isPresent()) {
            gameActions.add(new PlayAgainAction(mancala.getCurrentPlayer().getId()));
        } else {
            toggleTurn();
        }

    }

    //TODO: better name and improve function
    private void capturesOwnAndOppositeStones(Pit currentPit) {
        CaptureStoneAction captureStoneAction = new CaptureStoneAction();
        captureStoneAction.setOwnPitId(currentPit.getId());
        captureStoneAction.setOwnStone(currentPit.getStones());

        int pitIndex =  mancala.getCurrentPlayer().getPits().indexOf(currentPit);
        int totalStoneLargePit = mancala.getCurrentPlayer().getLargePit().getStones() + currentPit.getStones();
        currentPit.setStones(0);

        int nextIndex = mancala.getCurrentPlayerIndex();
        nextIndex ++;

        Pit oppositePit = mancala.getPlayers().get(nextIndex).getPits().get(pitIndex);
        captureStoneAction.setOppositePitId(oppositePit.getId());
        captureStoneAction.setOppositeStone(oppositePit.getStones());
        totalStoneLargePit += oppositePit.getStones();
        oppositePit.setStones(0);

        gameActions.add(captureStoneAction);

        mancala.getCurrentPlayer().getLargePit().setStones(totalStoneLargePit);
    }

    private Pit getStartPit(UUID id) {
        return findCurrentPlayerPitById(id)
                .orElseThrow(() ->
                        new RuntimeException(String.format("There is no pit with uuid %s for playerId %s",
                                id, mancala.getCurrentPlayer().getId())
                        )
                );
    }

    private Optional<Pit> findCurrentPlayerPitById(UUID id) {
        return mancala.getCurrentPlayer()
                .getPits()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    private Player getWinner() {
        for (Player player : mancala.getPlayers()) {
            int sum = player.getPits().stream().filter(x -> !x.equals(player.getLargePit())).mapToInt(x -> x.getStones()).sum();

            if(sum == 0) {
                this.gameActions.add(new WinnerAction(player.getId()));
                return player;
            }
        }
        return null;
    }

    public void endGame() {
        if(this.mancala == null) throw new RuntimeException("There is no game started");
        this.mancala = null;
        this.gameActions = null;
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
