package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MancalaGameDTO {
    int numberOfPits;
    PlayerDTO player1;
    PlayerDTO player2;

    UUID currentPlayerId;

}
