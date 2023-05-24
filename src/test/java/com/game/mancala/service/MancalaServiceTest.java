package com.game.mancala.service;

import com.game.mancala.dao.IMancalaGameDAO;
import com.game.mancala.dao.MancalaGameDAO;
import com.game.mancala.exception.MancalaException;
import com.game.mancala.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class MancalaServiceTest {
    @Autowired
    IMancalaGameDAO mancalaGameDAO;

    @Autowired
    MancalaService mancalaService;

    @BeforeEach
    public void init() {
        this.mancalaGameDAO = new MancalaGameDAO();
        this.mancalaService = new MancalaService(this.mancalaGameDAO);
    }

    @AfterEach
    public void finish(){
        // Run after each test because the MancalaGame needs to be detroyed
        if(mancalaService.isGameStarted()) {
            mancalaService.endGame();
        }

    }

    @Test
    public void should_returnGame_when_getGame() {
        //given
        mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        MancalaGame mancalaGame = mancalaService.getGame();

        //then
        Assertions.assertNotEquals(null, mancalaGame);
        Assertions.assertTrue(mancalaGame instanceof MancalaGame);
    }

    @Test
    public void should_throwException_when_getGameNotStarted() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.getGame();
        });

        String expectedMessage = "There is no game started";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_numberOfPitsIsLessOrEqualThanZero() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays.asList("p1", "p2"), 0, 6);
        });

        String expectedMessage = "Pits should be bigger than 0";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_numberOfStonesIsLessOrEqualThanZero() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 0);
        });

        String expectedMessage = "Stones should be bigger than 0";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_numberOfPitsIsBiggerThanThirteen() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays.asList("p1", "p2"), 13, 6);
        });

        String expectedMessage = "Pits should be less than 13";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_numberOfStonesIsBiggerThanThirteen() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 13);
        });

        String expectedMessage = "Stones should be less than 13";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_playersUsernameNull() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(null, 6, 6);
        });

        String expectedMessage = "PlayersUsername cannot be null";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_playersUsernameLessThanTwo() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays.asList("p1"), 6, 6);
        });

        String expectedMessage = "PlayersUsername should be more than 2";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_playersUsernameBiggerOrEqualThanEleven() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays
                    .asList("p1","p2","p3", "p4", "p5","p6", "p7","p8","p9", "p10", "p11"),
                    6,
                    6);
        });

        String expectedMessage = "PlayersUsername should be less than 11";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_startGame_when_gameIsNotStarted() {
        //assert
        Assertions.assertNotEquals(null, mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6));
    }

    @Test
    public void should_throwException_when_gameHasAlreadyStarted() {
        //given
        mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.startGame(Arrays.asList("pA", "pB"), 6, 6);
        });

        String expectedMessage = "A game is already running";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_returnFalse_when_gameIsNotYetStarted() {
        //assert
        Assertions.assertFalse(mancalaService.isGameStarted());
    }

    @Test
    public void should_returnTrue_when_gameIsNotAlreadyStarted() {
        //given
        mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //assert
        Assertions.assertTrue(mancalaService.isGameStarted());
    }

    @Test
    public void should_endGame_when_gameIsRunning() {
        //given
        mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        mancalaService.endGame();

        //then
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.getGame();
        });

        String expectedMessage = "There is no game started";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_gameHasAlreadyEnded() {
        //given
        mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        mancalaService.endGame();

        //then
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.getGame();
        });

        String expectedMessage = "There is no game started";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_throwException_when_gameHasNotYetStarted() {
        //when
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.endGame();
        });

        String expectedMessage = "There is no game started";
        String actualMessage = exception.getMessage();

        //then
        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void should_placeTheStoneInEachOwnPit_whenFirstMoveFromPit0() {
        //given
        MancalaGame mancalaGame =
                mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        mancalaService
                .getActions(mancalaGame.getCurrentPlayer().getPits().get(0).getId());

        MancalaGame getMancalaGame = mancalaService.getGame();
        Player currentPlayer = getMancalaGame.getCurrentPlayer();

        //then
        Assertions.assertTrue(currentPlayer.getPits().get(0).getStones() == 0);
        Assertions.assertTrue(currentPlayer.getPits().get(1).getStones() == 7);
        Assertions.assertTrue(currentPlayer.getPits().get(2).getStones() == 7);
        Assertions.assertTrue(currentPlayer.getPits().get(3).getStones() == 7);
        Assertions.assertTrue(currentPlayer.getPits().get(4).getStones() == 7);
        Assertions.assertTrue(currentPlayer.getPits().get(5).getStones() == 7);
        Assertions.assertTrue(currentPlayer.getLargePit().getStones() == 1);

    }

    @Test
    public void should_addTwoStonesToLargePit_whenMakeTheTwoFirstMoves() {
        //given
        MancalaGame mancalaGame =
                mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        mancalaService.getActions(mancalaGame.getCurrentPlayer().getPits().get(0).getId());
        mancalaService.getActions(mancalaGame.getCurrentPlayer().getPits().get(1).getId());
        Player playerToBeChecked = mancalaService.getGame().getPlayers().get(findOpponentIndex(mancalaGame));

        //then
        Assertions.assertTrue(playerToBeChecked.getLargePit().getStones() == 2);
    }

    @Test
    public void should_notAddStonesToOpponentLargePit_whenMakeTheMoves() {
        //given
        MancalaGame mancalaGame =
                mancalaService.startGame(Arrays.asList("p1", "p2"), 2, 10);

        //when
        Player firstPlayer = mancalaGame.getCurrentPlayer();
        mancalaService.getActions(mancalaGame.getCurrentPlayer().getPits().get(0).getId());


        int totalOpponentsLargePitSum = mancalaGame.getPlayers()
                .stream()
                .filter(x -> x.getId() != firstPlayer.getId())
                .mapToInt(x -> x.getLargePit().getStones())
                .sum();
        //then
        Assertions.assertEquals(totalOpponentsLargePitSum, 0);
    }

    @Test
    public void should_beOpponentPlayerTime_whenLastStoneDroppedInTheOpponentPit() {
        //given
        MancalaGame mancalaGame =
                mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        Player firstPlayer = mancalaGame.getCurrentPlayer();

        //when
        mancalaService.getActions(firstPlayer.getPits().get(5).getId());
        Player playerToBeChecked = mancalaService.getGame().getCurrentPlayer();

        //then
        Assertions.assertNotEquals(firstPlayer.getId(), playerToBeChecked.getId());
    }

    @Test
    public void should_returnTieAction_whenGameIsTied(){
        MancalaGame mancalaGame = mancalaService.startGame(Arrays.asList("p1", "p2"), 1, 1);
        List<Action> actions = mancalaService.getActions(mancalaGame.getCurrentPlayer().getPits().get(0).getId());

        Assertions.assertEquals(actions.stream().filter(x -> x.getActionType() == ActionType.TIE).count(), 1);
    }


    @Test
    public void should_returnGameOverAndWinner_whenLastAction() {
        //given
        MancalaGame mancalaGame =
                mancalaService.startGame(Arrays.asList("p1", "p2"), 2, 1);

        //when
        Player winnerPlayer = mancalaGame.getCurrentPlayer();
        mancalaService.getActions(winnerPlayer.getPits().get(1).getId()); //A1
        List<Action> actions = mancalaService.getActions(winnerPlayer.getPits().get(0).getId()); //b0

        Optional<Action> winnerAction = actions.stream()
                .filter(x -> x.getActionType() == ActionType.WINNER).findFirst();

        //then
        Assertions.assertTrue(mancalaGame.isGameOver());
        Assertions.assertEquals(actions.stream()
                .filter(x -> x.getActionType() == ActionType.GAME_OVER).count(), 1);
        Assertions.assertTrue(winnerAction.isPresent());
        Assertions.assertEquals(winnerPlayer.getId(), ((WinnerAction) winnerAction.get()).getWinnerPlayerId());
    }

    @Test
    public void should_returnCaptureStonesAction_whenTheOponnentPitIsEmpty() {
        //given
        MancalaGame mancalaGame =
                mancalaService.startGame(Arrays.asList("p1", "p2"), 2, 1);

        //when
        Player winnerPlayer = mancalaGame.getCurrentPlayer();
        mancalaService.getActions(winnerPlayer.getPits().get(1).getId()); //A1
        List<Action> actions = mancalaService.getActions(winnerPlayer.getPits().get(0).getId()); //b0

        CaptureStoneAction captureStonesAction = (CaptureStoneAction) actions.stream()
                .filter(x -> x.getActionType() == ActionType.CAPTURE_STONES).findFirst().get();

        //then
        Assertions.assertEquals(captureStonesAction.getRemovedOwnPitStonesQuantity(), 1);
        Assertions.assertEquals(captureStonesAction.getRemovedOppositeStonesQuantity(), 1);
        Assertions.assertEquals(captureStonesAction.getAddedLargePitStonesQuantity(), 2);
    }

    private int findOpponentIndex(MancalaGame mancala) {
        int opponentPlayerIndex = mancala.getCurrentPlayerIndex();
        opponentPlayerIndex ++;
        if(opponentPlayerIndex >= mancala.getPlayers().size()) {
            opponentPlayerIndex = 0;
        }

        return  opponentPlayerIndex;
    }

}
