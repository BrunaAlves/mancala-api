package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MancalaGameDTO {
    private int numberOfPits;
    private List<PlayerDTO> players;
    private UUID currentPlayerId;

}
