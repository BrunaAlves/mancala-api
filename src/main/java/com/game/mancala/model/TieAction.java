package com.game.mancala.model;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class TieAction  extends Action {
    private List<UUID> tiedPlayersId;

    public TieAction(List<UUID> tiedPlayersId) {
        super.setActionType(ActionType.TIE);
        this.tiedPlayersId = tiedPlayersId;
    }
}