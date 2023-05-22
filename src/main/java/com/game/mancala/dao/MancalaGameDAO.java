package com.game.mancala.dao;

import com.game.mancala.model.MancalaGame;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MancalaGameDAO implements IMancalaGameDAO {

    private MancalaGame mancalaGame = null;

    public MancalaGameDAO() {
    }

    public MancalaGameDAO(MancalaGame mancalaGame) {
        this.mancalaGame = mancalaGame;
    }

    @Override
    public List<MancalaGame> getAll() {
        return null;
    }

    @Override
    public MancalaGame save(MancalaGame mancalaGame) {
        this.mancalaGame = mancalaGame;
        return mancalaGame;
    }

    @Override
    public void delete(MancalaGame mancalaGame) {
        this.mancalaGame = null;
    }

    @Override
    public Optional<MancalaGame> getGame() {
        return Optional.ofNullable(this.mancalaGame);
    }

}
