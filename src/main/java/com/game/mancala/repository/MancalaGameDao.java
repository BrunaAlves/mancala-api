package com.game.mancala.repository;

import com.game.mancala.model.MancalaGame;

import java.util.List;
import java.util.Optional;

public class MancalaGameDao implements Dao<MancalaGame> {

    private MancalaGame mancalaGame = null;

    public MancalaGameDao() {
    }

    public MancalaGameDao(MancalaGame mancalaGame) {
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
