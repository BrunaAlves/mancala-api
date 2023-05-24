package com.game.mancala.service;

import com.game.mancala.dao.IMancalaGameDAO;
import com.game.mancala.exception.MancalaException;
import com.game.mancala.model.*;
import com.game.mancala.dao.ActionDAO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MancalaService {

    private ActionDAO actionDAO;

    private IMancalaGameDAO mancalaGameDAO;

    public MancalaService(IMancalaGameDAO mancalaGameDAO) {
        this.mancalaGameDAO = mancalaGameDAO;
    }

    private static final String MESSAGE_GAME_RUNNING = "A game is already running";
    private static final String MESSAGE_NO_GAME_STARTED = "There is no game started";
    private static final String MESSAGE_NO_PIT_STONE = "There is no stones on pit %s";
    private static final String MESSAGE_NO_PIT_FOR_PLAYER_ID = "There is no pit with uuid %s for playerId %s";

    public MancalaGame getGame(){
        return mancalaGameDAO.getGame()
                .orElseThrow(() -> {throw new MancalaException(MESSAGE_NO_GAME_STARTED);});

    }

    public MancalaGame startGame(List<String> listOfPlayersUsername, int numberOfPits, int numberOfStones) {
        if(isGameStarted()) throw new MancalaException(MESSAGE_GAME_RUNNING);
        MancalaGame mancala = mancalaGameDAO.save(new MancalaGame(listOfPlayersUsername,numberOfPits,numberOfStones));
        this.actionDAO = new ActionDAO();

        Random random = new Random();
        setTurn(random.nextInt(mancala.getPlayers().size()));
        return mancala;
    }

    public boolean isGameStarted(){
        return mancalaGameDAO != null && mancalaGameDAO.getGame().isPresent();
    }

    public void endGame() {
        if(!isGameStarted()) throw new MancalaException(MESSAGE_NO_GAME_STARTED);
        this.mancalaGameDAO.delete(this.getGame());
        this.actionDAO.deleteAll();
    }

    public List<Action> getActions(UUID id){
        MancalaGame mancala = this.getGame();
        List<Action> actions = new ArrayList<>();
        Pit startPit = getStartPit(id);

        if(startPit.getStones() == 0) {
            throw new MancalaException(String.format(MESSAGE_NO_PIT_STONE,startPit.getName()));
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
        boolean isAllowedToPlayAgain = true;
        boolean endOnMyOwnPit = findCurrentPlayerPitById(currentPit.getId()).isPresent();
        if(endOnMyOwnPit &&
                currentPit.getStones() == 1 &&
                !currentPit.equals(mancala.getCurrentPlayer().getLargePit())) {
            actions.add(capturesOwnAndOppositeStones(currentPit));
            isAllowedToPlayAgain = false;
        }

        if(isGameOver()) {
            actions.add(new GameOverAction(mancala.getPlayers()));
            mancala.setGameOver(true);
            actions.addAll(findWinner());
        } else {
            if(endOnMyOwnPit && isAllowedToPlayAgain){
                actions.add(new PlayAgainAction(mancala.getCurrentPlayer().getId()));
            }else {
                toggleTurn();
                actions.add(new NextPlayerAction(mancala.getCurrentPlayer().getId()));
            }
        }

        mancalaGameDAO.save(mancala);
        actionDAO.addAll(actions);
        return actions;
    }

    public List<Action> getAllActions() { return actionDAO.getAll();}

    private boolean isGameOver() {
        MancalaGame mancala = this.getGame();
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
        MancalaGame mancala = this.getGame();
        int pitIndex =  mancala.getCurrentPlayer().getPits().indexOf(currentPit);
        int totalStonesToAdd =  currentPit.getStones();

        Pit oppositePit = mancala.getPlayers()
                .get(this.findOpponentIndex()).getPits().get(mancala.getNumberOfPits() - 1 - pitIndex);

        totalStonesToAdd += oppositePit.getStones();
        int totalStoneLargePit = mancala.getCurrentPlayer().getLargePit().getStones() + totalStonesToAdd;

        CaptureStoneAction captureStoneAction =
                new CaptureStoneAction(currentPit.getStones(),
                        oppositePit.getStones(),
                        totalStonesToAdd,
                        currentPit.getId(),
                        oppositePit.getId(),
                        mancala.getCurrentPlayer().getLargePit().getId());

        currentPit.setStones(0);
        oppositePit.setStones(0);
        mancala.getCurrentPlayer().getLargePit().setStones(totalStoneLargePit);
        mancalaGameDAO.save(mancala);
        return captureStoneAction;
    }

    private Pit getStartPit(UUID id) {
        MancalaGame mancala = this.getGame();
        return findCurrentPlayerPitById(id)
                .orElseThrow(() ->
                        new MancalaException(String.format(MESSAGE_NO_PIT_FOR_PLAYER_ID,
                                id, mancala.getCurrentPlayer().getId())
                        )
                );
    }

    private Optional<Pit> findCurrentPlayerPitById(UUID id) {
        MancalaGame mancala = this.getGame();
        return mancala.getCurrentPlayer()
                .getPits()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    private List<Action> findWinner() {
        MancalaGame mancala = this.getGame();
        List<Action> actions = new ArrayList<>();

        for (Player player : mancala.getPlayers()) {
            for (int i = 0; i <= player.getPits().size() - 2; i++) {
                Pit pit = player.getPits().get(i);
                if(pit.getStones() > 0) {
                    actions.add(new MoveAction(pit.getId(), player.getLargePit().getId(), pit.getStones(), pit.getStones()));
                    player.getLargePit().setStones(player.getLargePit().getStones() + pit.getStones());
                    pit.setStones(0);
                }
            }
        }

        List<Player> sortedWinners = mancala.getPlayers()
                .stream()
                .sorted((a, b) -> a.getLargePit().getStones() > b.getLargePit().getStones() ? -1 : 1)
                .collect(Collectors.toList());
        List<Player> winners = sortedWinners
                .stream()
                .filter(x -> x.getLargePit().getStones() == sortedWinners.get(0).getLargePit().getStones())
                .collect(Collectors.toList());

        if(winners.size() == 1){
            actions.add(new WinnerAction(winners.get(0).getId()));
        }else{
            actions.add(new TieAction(winners.stream().map(x -> x.getId()).collect(Collectors.toList())));
        }

        mancalaGameDAO.save(mancala);
        return actions;
    }

    private int findOpponentIndex() {
        MancalaGame mancala = this.getGame();
        int opponentPlayerIndex = mancala.getCurrentPlayerIndex();
        opponentPlayerIndex ++;
        if(opponentPlayerIndex >= mancala.getPlayers().size()) {
            opponentPlayerIndex = 0;
        }

        return  opponentPlayerIndex;
    }

    private void setTurn(int index) {
        MancalaGame mancala = this.getGame();
        mancala.setCurrentPlayerIndex(index);
        mancalaGameDAO.save(mancala);
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
