package com.game.mancala.controller;

import com.game.mancala.model.Mancala;
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
    public ResponseEntity<Mancala> getName(){
        MancalaGame mancalaGame = mancalaService.get();
        if(mancalaGame == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(mancalaGame);
    }

    @PostMapping("/start")
    public ResponseEntity<Mancala> start(){
        return ResponseEntity.status(HttpStatus.CREATED).body(mancalaService.startGame());
    }

    @DeleteMapping("/end")
    public ResponseEntity<Mancala> end(){
        mancalaService.endGame();
        return ResponseEntity.ok().build();
    }
}

