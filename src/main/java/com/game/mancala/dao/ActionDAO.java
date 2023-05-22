package com.game.mancala.dao;

import com.game.mancala.model.Action;
import java.util.ArrayList;
import java.util.List;

public class ActionDAO implements IDAO<Action> {
    private List<Action> actions = new ArrayList<>();

    @Override
    public List<Action> getAll() {
        return this.actions;
    }

    @Override
    public Action save(Action action) {
        this.actions.add(action);
        return action;
    }

    @Override
    public void delete(Action action) {

    }

    public void addAll(List<Action> actions) {
        this.actions.addAll(actions);
    }

}
