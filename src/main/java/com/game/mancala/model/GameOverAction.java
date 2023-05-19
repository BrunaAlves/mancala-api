package com.game.mancala.model;

import lombok.Getter;

import java.util.List;

@Getter
public class GameOverAction extends Action {
    private List<Player> players;

    public GameOverAction(List<Player> players) {
        super.setActionType(ActionType.GAME_OVER);
        this.players = players;
    }
}
