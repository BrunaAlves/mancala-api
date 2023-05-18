package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MancalaGameDTO {
    int numberOfPits;
    List<PlayerDTO> players;

    UUID currentPlayerId;

}
