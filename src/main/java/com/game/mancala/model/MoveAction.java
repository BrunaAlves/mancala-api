package com.game.mancala.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MoveAction extends Action {
    private UUID fromPitId;
    private UUID toPitId;
    private int moveQuantity;
    private int handQuantity;

    public MoveAction(UUID fromPitId, UUID toPitId, int moveQuantity, int handQuantity) {
        super.setActionType(ActionType.MOVE);
        this.fromPitId = fromPitId;
        this.toPitId = toPitId;
        this.moveQuantity = moveQuantity;
        this.handQuantity = handQuantity;
    }
}
