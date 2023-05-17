package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerDTO {
    UUID id;
    String username;
    PitDTO largePit;
    List<PitDTO> pits;
}
