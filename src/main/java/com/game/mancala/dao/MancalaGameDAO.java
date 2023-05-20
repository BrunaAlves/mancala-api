package com.game.mancala.dao;

import com.game.mancala.model.MancalaGame;

import java.util.List;
import java.util.Optional;

public class MancalaGameDAO implements DAO<MancalaGame> {

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
    public void save(MancalaGame mancalaGame) {
        this.mancalaGame = mancalaGame;

    }

    public Optional<MancalaGame> getGame() {
        return Optional.ofNullable(this.mancalaGame);
    }

}
