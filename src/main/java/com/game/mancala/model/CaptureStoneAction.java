package com.game.mancala.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class CaptureStoneAction extends Action {
    private int removedOwnPitStonesQuantity;
    private int removedOppositeStonesQuantity;
    private int addedLargePitStonesQuantity;
    private UUID ownPitId;
    private UUID oppositePitId;
    private UUID largePitId;

    public CaptureStoneAction(int removedOwnPitStonesQuantity, int removedOppositeStonesQuantity,
                              int addedLargePitStonesQuantity, UUID ownPitId,
                              UUID oppositePitId, UUID largePitId) {
        super.setActionType(ActionType.CAPTURE_STONES);
        this.removedOwnPitStonesQuantity = removedOwnPitStonesQuantity;
        this.removedOppositeStonesQuantity = removedOppositeStonesQuantity;
        this.addedLargePitStonesQuantity = addedLargePitStonesQuantity;
        this.ownPitId = ownPitId;
        this.oppositePitId = oppositePitId;
        this.largePitId = largePitId;
    }
}
