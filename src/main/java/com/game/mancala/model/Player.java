package com.game.mancala.model;

import com.game.mancala.dto.MancalaGameDTO;
import com.game.mancala.dto.PlayerDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class Player{
    private UUID id;
    private String username;
    private Pit largePit;
    private List<Pit> pits = null;

    public Player(String username, int numberOfPits, int numberOfStones) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.pits = new ArrayList<>();
        this.largePit = new Pit(this.username + ":large", 0);

        for (int i = 0; i < numberOfPits; i++) {
            this.pits.add(new Pit(this.username + ":" + i, numberOfStones));

        }
        this.pits.add(this.largePit);
    }

    public PlayerDTO toEntityDTO(){
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(this.id);
        playerDTO.setUsername(this.username);
        playerDTO.setLargePit(this.largePit.toEntityDTO());
        playerDTO.setPits(this.pits.stream().map(pit -> pit.toEntityDTO()).collect(Collectors.toList()));
        return playerDTO;

    }
}
