package com.game.mancala.model;

import com.game.mancala.dto.PitDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pit {
    private String name;
    private Integer stones = null;

    public Pit(String name, int stones) {
        this.name = name;
        this.stones = stones;
    }

    public PitDTO toEntityDTO(){
        PitDTO pitDTO = new PitDTO();
        pitDTO.setName(this.name);
        pitDTO.setStones(this.stones);
        return pitDTO;

    }
}
