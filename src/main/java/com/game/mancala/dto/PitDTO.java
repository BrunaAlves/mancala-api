package com.game.mancala.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PitDTO {
    UUID id;
    String name;
    int stones;
}
