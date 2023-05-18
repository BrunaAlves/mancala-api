package com.game.mancala.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayAgainAction extends Action {
    private UUID PlayAgainPlayerId;

    public PlayAgainAction(UUID playAgainPlayerId) {
        super.setActionType(ActionType.PLAY_AGAIN);
        PlayAgainPlayerId = playAgainPlayerId;
    }
}
