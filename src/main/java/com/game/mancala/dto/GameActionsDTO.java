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
        super.currentPlayerId = mancalaGameDTO.currentPlayerId;
        super.numberOfPits = mancalaGameDTO.numberOfPits;
        this.players = mancalaGameDTO.getPlayers();
        this.actions = actions;
    }
}
