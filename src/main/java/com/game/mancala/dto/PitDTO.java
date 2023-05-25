package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PitDTO {
    private UUID id;
    private String name;
    private int stones;
}
