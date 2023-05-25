package com.game.mancala.dto;

import com.game.mancala.model.Action;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameActionsDTO extends MancalaGameDTO{
    private List<Action> actions;

    public GameActionsDTO(List<Action> actions, MancalaGameDTO mancalaGameDTO) {
        super.setCurrentPlayerId(mancalaGameDTO.getCurrentPlayerId());
        super.setNumberOfPits(mancalaGameDTO.getNumberOfPits());
        super.setPlayers(mancalaGameDTO.getPlayers());
        this.actions = actions;
    }
}
