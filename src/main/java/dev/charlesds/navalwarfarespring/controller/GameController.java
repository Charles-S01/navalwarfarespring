package dev.charlesds.navalwarfarespring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.charlesds.navalwarfarespring.entity.Coordinate;
import dev.charlesds.navalwarfarespring.entity.Game;
import dev.charlesds.navalwarfarespring.entity.Gameboard;
import dev.charlesds.navalwarfarespring.entity.Player;
import dev.charlesds.navalwarfarespring.entity.Ship;
import dev.charlesds.navalwarfarespring.repository.CoordinateRepository;
import dev.charlesds.navalwarfarespring.repository.GameRepository;
import dev.charlesds.navalwarfarespring.repository.GameboardRepository;
import dev.charlesds.navalwarfarespring.repository.PlayerRepository;
import dev.charlesds.navalwarfarespring.repository.ShipRepository;
import dev.charlesds.navalwarfarespring.request.PlaceShipRequest;
import dev.charlesds.navalwarfarespring.response.Event;
import dev.charlesds.navalwarfarespring.response.GameUpdate;
import dev.charlesds.navalwarfarespring.service.GameService;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "${allowed.origin}")
@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameboardRepository gameboardRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private CoordinateRepository coordinateRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameService gameService;
    
    @PostMapping
    public ResponseEntity<Object> createGame() {
        return gameService.createGame();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Object> getGame(@PathVariable String gameId) {
        return gameService.getGameById(gameId);
    }

    @PostMapping("/join")
    public ResponseEntity<Object> joinGame(@RequestBody Map<String, Object> body) {
        return gameService.joinGame(body);
    }

    @PostMapping("/ships")
    public ResponseEntity<Object> placeShips(@RequestBody PlaceShipRequest body) {
        return gameService.placeShips(body);
    }
    

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Object> deleteGame(@PathVariable String gameId) {
        return gameService.deleteGameById(gameId);
    }
    
}
