package com.game.mancala.repository;

import com.game.mancala.model.Action;
import java.util.ArrayList;
import java.util.List;

public class ActionDao implements Dao<Action> {
    private List<Action> actions = new ArrayList<>();

    @Override
    public List<Action> getAll() {
        return this.actions;
    }

    @Override
    public void save(Action action) {
        this.actions.add(action);
    }

    public void addAll(List<Action> actions) {
        this.actions.addAll(actions);
    }

}
