package com.game.mancala.dao;

import com.game.mancala.model.MancalaGame;

import java.util.Optional;

public interface IMancalaGameDAO extends IDAO<MancalaGame> {
    Optional<MancalaGame> getGame();
    void delete(MancalaGame mancalaGame);
}
