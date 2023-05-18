package com.game.mancala.model;

import com.game.mancala.dto.PitDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Pit {
    private UUID id;
    private String name;
    private Integer stones = null;

    public Pit(String name, int stones) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.stones = stones;
    }

    public PitDTO toEntityDTO(){
        PitDTO pitDTO = new PitDTO();
        pitDTO.setId(this.id);
        pitDTO.setName(this.name);
        pitDTO.setStones(this.stones);
        return pitDTO;

    }
}
