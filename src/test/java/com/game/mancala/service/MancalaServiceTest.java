package com.game.mancala.service;

import com.game.mancala.dao.IMancalaGameDAO;
import com.game.mancala.dao.MancalaGameDAO;
import com.game.mancala.exception.MancalaException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;

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
    public void should_throwException_when_numberOfPitsIsLessAndEqualThanZero() {
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
    public void should_throwException_when_numberOfStonesIsLessAndEqualThanZero() {
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
    public void should_endGameAndThrowException_when_gameHasStarted() {
        //given
        mancalaService.startGame(Arrays.asList("p1", "p2"), 6, 6);

        //when
        mancalaService.endGame();

        //then
        MancalaException exception = assertThrows(MancalaException.class, () -> {
            mancalaService.get();
        });

        String expectedMessage = "There is no game started";
        String actualMessage = exception.getMessage();

        //then
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


}
