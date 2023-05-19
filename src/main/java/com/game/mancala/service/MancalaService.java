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
        if(mancala.isGameOver()) throw new RuntimeException("The game is over, please start a new one");

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


        //Capture all stones
        if(findCurrentPlayerPitById(currentPit.getId()).isPresent()) {
            if(currentPit.getStones() == 1 && !currentPit.equals(mancala.getCurrentPlayer().getLargePit())) {
                actions.add(capturesOwnAndOppositeStones(currentPit));
            } else {
                if(!isGameOver()) {
                    actions.add(new PlayAgainAction(mancala.getCurrentPlayer().getId()));
                }
            }

        } else {
            if(isGameOver()) {
                actions.add(new GameOverAction(mancala.getPlayers()));
                mancala.setGameOver(true);
                actions.addAll(findWinner());

            } else {
                toggleTurn();
                actions.add(new NextPlayerAction(mancala.getCurrentPlayer().getId()));
            }

        }

        gameActions.addAll(actions);
        return new GameActionsDTO(actions, mancala.toEntityDTO());

    }

    private boolean isGameOver() {
        for (Player player : mancala.getPlayers()) {
            int sum = player.getPits().stream()
                    .filter(x -> !x.equals(player.getLargePit()))
                    .mapToInt(x -> x.getStones()).sum();

            if(sum == 0) {
                return true;
            }
        }

        return false;
    }

    //TODO: better name and improve function
    private CaptureStoneAction capturesOwnAndOppositeStones(Pit currentPit) {
        int pitIndex =  mancala.getCurrentPlayer().getPits().indexOf(currentPit);
        int totalStoneLargePit = mancala.getCurrentPlayer().getLargePit().getStones() + currentPit.getStones();

        Pit oppositePit = mancala.getPlayers()
                .get(this.findOpponentIndex()).getPits().get(mancala.getNumberOfPits() - 1 - pitIndex);

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

    private List<Action> findWinner() {
        List<Action> actions = new ArrayList<>();
        Player opponent = mancala.getPlayers().get(this.findOpponentIndex());

        for (int i = opponent.getPits().size() - 2; i >= 0; i--) {
            Pit pit = opponent.getPits().get(i);
            actions.add(new MoveAction(pit.getId(), opponent.getLargePit().getId(), pit.getStones(), pit.getStones()));
            opponent.getLargePit().setStones(opponent.getLargePit().getStones() + pit.getStones());
            pit.setStones(0);
        }

        Player player1 = mancala.getPlayers().get(0);
        Player player2 = mancala.getPlayers().get(1);

        if(player1.getLargePit().getStones() > player2.getLargePit().getStones()) {
            actions.add(new WinnerAction(player1.getId()));
        } else {
            actions.add(new WinnerAction(player2.getId()));
        }

        return actions;
    }

    private int findOpponentIndex() {
        int opponentPlayerIndex = mancala.getCurrentPlayerIndex();
        opponentPlayerIndex ++;
        if(opponentPlayerIndex >= mancala.getPlayers().size()) {
            opponentPlayerIndex = 0;
        }

        return  opponentPlayerIndex;
    }

    private void setTurn(int index) {
        mancala.setCurrentPlayerIndex(index);
    }

    private void toggleTurn() {
        setTurn(this.findOpponentIndex());
    }

    private int emptyPit(Pit pit){
       int handStones = pit.getStones();
       pit.setStones(0);
       return handStones;
    }
}
