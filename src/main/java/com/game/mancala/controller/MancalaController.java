package com.game.mancala.controller;

import com.game.mancala.dto.MancalaGameDTO;
import com.game.mancala.model.MancalaGame;
import com.game.mancala.service.MancalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class MancalaController {

    private final MancalaService mancalaService;

    @GetMapping
    public ResponseEntity<MancalaGameDTO> getGame(){
        MancalaGame mancalaGame = mancalaService.get();
        if(mancalaGame == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(mancalaGame.toEntityDTO());
    }

    @PostMapping("/start")
    public ResponseEntity<MancalaGameDTO> start(){
        return ResponseEntity.status(HttpStatus.CREATED).body(mancalaService.startGame().toEntityDTO());
    }

    @DeleteMapping("/end")
    public ResponseEntity<?> end(){
        mancalaService.endGame();
        return ResponseEntity.ok().build();
    }
}
