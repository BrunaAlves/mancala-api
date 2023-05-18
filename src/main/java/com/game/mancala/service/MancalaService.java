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

    public List<Action> getGameActions() { return gameActions;}

    public MancalaGame startGame() {
        if(mancala != null ) throw new RuntimeException("A game is already running");
        this.mancala = new MancalaGame(List.of("player1", "player2"),6,6);
        this.gameActions = new ArrayList<>();

        Random random = new Random();
        setTurn(random.nextInt(mancala.getPlayers().size()));
        return mancala;
    }

    public void endGame() {
        if(this.mancala == null) throw new RuntimeException("There is no game started");
        this.mancala = null;
        this.gameActions = null;
    }

    //TODO: improve function
    public GameActionsDTO getActions(UUID id){
        List<Action> actions = new ArrayList<>();
        Pit startPit = getStartPit(id);

        if(startPit.getStones() == 0) {
            throw new RuntimeException(String.format("There is no stones on pit %s",startPit.getName()));
        }

        int pitIndex =  mancala.getCurrentPlayer().getPits().indexOf(startPit);
        int handStones = emptyPit(startPit);
        int playerIndex = mancala.getCurrentPlayerIndex();
        Pit currentPit = startPit;

        while (handStones > 0){
            pitIndex ++;

            if(pitIndex >= mancala.getPlayers().get(playerIndex).getPits().size()) {
                pitIndex = 0;
                playerIndex ++;
                if(playerIndex >= mancala.getPlayers().size()) {
                    playerIndex = 0;
                }
            }

            currentPit = mancala.getPlayers().get(playerIndex).getPits().get(pitIndex);

            if(currentPit.equals(mancala.getPlayers().get(playerIndex).getLargePit())) {
                if(currentPit.equals(mancala.getCurrentPlayer().getLargePit())) {
                    currentPit.setStones(currentPit.getStones() + 1);
                    handStones --;
                    actions.add(new MoveAction(startPit.getId(), currentPit.getId(),1, handStones));
                }
            } else {
                currentPit.setStones(currentPit.getStones() + 1);
                handStones --;
                actions.add(new MoveAction(startPit.getId(), currentPit.getId(),1, handStones));
            }
        }

        Player winner = getWinner();
        System.out.println(winner);

        if(findCurrentPlayerPitById(currentPit.getId()).isPresent()) {
            if(currentPit.getStones() == 1 && !currentPit.equals(mancala.getCurrentPlayer().getLargePit())) {
                actions.add(capturesOwnAndOppositeStones(currentPit));
            } else {
                actions.add(new PlayAgainAction(mancala.getCurrentPlayer().getId()));
            }

        } else {
            toggleTurn();
        }

        actions.add(new NextPlayerAction(mancala.getCurrentPlayer().getId()));
        gameActions.addAll(actions);
        return new GameActionsDTO(actions, mancala.toEntityDTO());

    }

    //TODO: better name and improve function
    private CaptureStoneAction capturesOwnAndOppositeStones(Pit currentPit) {
        int pitIndex =  mancala.getCurrentPlayer().getPits().indexOf(currentPit);
        int totalStoneLargePit = mancala.getCurrentPlayer().getLargePit().getStones() + currentPit.getStones();

        int nextPlayerIndex = mancala.getCurrentPlayerIndex();
        nextPlayerIndex ++;
        if(nextPlayerIndex >= mancala.getPlayers().size()) {
            nextPlayerIndex = 0;
        }

        Pit oppositePit = mancala.getPlayers().get(nextPlayerIndex).getPits().get(mancala.getNumberOfPits() - 1 - pitIndex);
        totalStoneLargePit += oppositePit.getStones();


        CaptureStoneAction captureStoneAction =
                new CaptureStoneAction(currentPit.getStones(),
                        oppositePit.getStones(),
                        currentPit.getId(),
                        oppositePit.getId());

        currentPit.setStones(0);
        oppositePit.setStones(0);
        mancala.getCurrentPlayer().getLargePit().setStones(totalStoneLargePit);
        return captureStoneAction;
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
