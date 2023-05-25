package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerDTO {
    private UUID id;
    private String username;
    private PitDTO largePit;
    private List<PitDTO> pits;
}
