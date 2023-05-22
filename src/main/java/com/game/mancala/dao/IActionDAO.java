package com.game.mancala.dao;

import com.game.mancala.model.Action;

import java.util.List;

public interface IActionDAO extends IDAO<Action>{
    void deleteAll();
    List<Action> addAll(List<Action> actions);

}
