package com.game.mancala.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class WinnerAction extends Action {
    private UUID winnerPlayerId;

    public WinnerAction(UUID winnerPlayerId) {
        super.setActionType(ActionType.WINNER);
        this.winnerPlayerId = winnerPlayerId;
    }
}
