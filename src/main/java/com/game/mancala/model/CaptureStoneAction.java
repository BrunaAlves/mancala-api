package com.game.mancala.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class CaptureStoneAction extends Action{
    private int ownStone;
    private int OppositeStone;
    private UUID ownPitId;
    private UUID oppositePitId;

    public CaptureStoneAction(int ownStone, int oppositeStone, UUID ownPitId, UUID oppositePitId) {
        super.setActionType(ActionType.CAPTURE_STONES);
        this.ownStone = ownStone;
        OppositeStone = oppositeStone;
        this.ownPitId = ownPitId;
        this.oppositePitId = oppositePitId;
    }
}
