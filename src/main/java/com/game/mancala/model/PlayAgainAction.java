package com.game.mancala.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayAgainAction extends Action {
    private UUID playAgainPlayerId;

    public PlayAgainAction(UUID playAgainPlayerId) {
        super.setActionType(ActionType.PLAY_AGAIN);
        this.playAgainPlayerId = playAgainPlayerId;
    }
}
