package com.game.mancala.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NextPlayerAction extends Action{
    private UUID id;

    public NextPlayerAction(UUID id) {
        super.setActionType(ActionType.NEXT_PLAYER);
        this.id = id;
    }
}
