package dev.charlesds.navalwarfarespring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import dev.charlesds.navalwarfarespring.entity.AttackResult;
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
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class GameService {
    
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


    public ResponseEntity<Object> createGame() {
        System.out.println("createGame");

        try {
            Player player = new Player();
            player.addGameboard(new Gameboard());
            
            Game game = new Game();
            game.addPlayer(player);
            game.setPlayerTurnId(player.getId());
            
            gameRepository.save(game);
    
            Map<String, Object> res = new HashMap<>();
            // res.put("gameId", game.getId());
            res.put("playerId", player.getId());
            res.put("game", getGameEntity(game.getId()));
    
            return ResponseEntity.ok(res);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating game");
        }
    }

    public ResponseEntity<Object> joinGame(Map<String, Object> body) {
        try {
            String gameId = (String) body.get("gameId");
            Game game = gameRepository.findById(gameId).orElseThrow();

            int numberOfPlayers = game.getPlayers().size();
            if (numberOfPlayers == 2) {
                return ResponseEntity.badRequest().body("Game is full");
            }
            else if (numberOfPlayers == 1) {
                Player player = new Player();
                player.addGameboard(new Gameboard());
        
                game.addPlayer(player);
        
                gameRepository.save(game);
        
                simpMessagingTemplate.convertAndSend("/topic/public/room-"+gameId, new GameUpdate(Event.JOIN, getGameEntity(game.getId())));
                
                Map<String, Object> res = new HashMap<>();
                res.put("playerId", player.getId());
                
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.badRequest().body("Game does not exist");
    
        } catch (Exception e) {
            System.out.println("Error joining game: " + e.getMessage());
            return ResponseEntity.badRequest().body("Game does not exist");
        }

    }

    public ResponseEntity<Object> getGameById(String gameId) {
        System.out.println("GET GAME");

        try {
            Game game = gameRepository.findById(gameId).orElseThrow();
            Map<String, Object> res = new HashMap<>();
            res.put("game", getGameEntity(game.getId()));
    
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println("Error getting game: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error getting game");
        }

    }

    public ResponseEntity<Object> placeShips(PlaceShipRequest body) {
        try {
            String gameId = body.getGameId();
            String playerId = body.getPlayerId();
            List<Coordinate> coords = body.getCoords();
    
            Game game = gameRepository.findById(gameId).orElseThrow();
            Player player = playerRepository.findById(playerId).orElseThrow();

            Ship ship = new Ship();
            ship.setLength(coords.size());
            ship.setCoordinates(coords);
            player.getGameboard().addShip(ship);

            for (Coordinate c : coords) {
                c.setGameboard(player.getGameboard());
                c.setShip(ship);
            }
            player.getGameboard().setCoordinates(coords);
    
            gameRepository.save(game);
    
            List<Player> players = game.getPlayers();
            if (players.get(0).getGameboard().getShips().size() > 0 && players.get(1).getGameboard().getShips().size() > 0) {
                simpMessagingTemplate.convertAndSend("/topic/public/room-"+gameId, new GameUpdate(Event.START, getGameEntity(game.getId())));
                return ResponseEntity.ok(null);
            }
            Map<String, Object> res = new HashMap<>();
            res.put("game", getGameEntity(game.getId()));
    
            return ResponseEntity.ok(res);
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error placing ships: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error placing ships");
        }
    }

    @Transactional
    public void handleAttack(Map<String, Object> body) {
        System.out.println("handleAttack");
        System.out.println(body);
        try {
            String gameId = (String) body.get("gameId");
            String userId = (String) body.get("playerId");
            String oppId = (String) body.get("oppId");
            int row = (int) body.get("row");
            int col = (int) body.get("col");
            
            Player oppPlayer = playerRepository.findById(oppId).orElseThrow();
            Optional<Coordinate> targetCoord = coordinateRepository.findByGameboardIdAndRowAndCol(oppPlayer.getGameboard().getId(), row, col);

            if (targetCoord.isPresent() && targetCoord.get().getShip() != null) {
                System.out.println("HIT");
                Coordinate coord = targetCoord.get();
                coord.getShip().setLength(coord.getShip().getLength() - 1);
                coord.setAttackResult(AttackResult.HIT);
                
                if (coord.getShip().getLength() == 0) {
                    oppPlayer.getGame().setWinnerId(userId);
                }
            }
            else {
                System.out.println("MISS");
                Coordinate coord = new Coordinate();
                coord.setRow(row);
                coord.setCol(col);
                coord.setAttackResult(AttackResult.MISS);
                oppPlayer.getGameboard().addCoordinate(coord);
            }
            oppPlayer.getGame().setPlayerTurnId(oppId);

            gameRepository.save(oppPlayer.getGame());
            Game updatedGame = getGameEntity(oppPlayer.getGame().getId());

            simpMessagingTemplate.convertAndSend("/topic/public/room-"+oppPlayer.getGame().getId(), new GameUpdate(Event.ATTACK, updatedGame));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteGameById(String gameId) {
        System.out.println("deleteGame");
        try {
            gameRepository.deleteById(gameId);

            simpMessagingTemplate.convertAndSend("/topic/public/room-"+gameId, new GameUpdate(Event.END, null));

            return ResponseEntity.ok("ok");

        } catch (Exception e) {
            System.out.println("Error deleting game: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error deleting game");
        }
    }

    private Game getGameEntity(String gameId) {
        EntityGraph entityGraph = entityManager.getEntityGraph("game-entity-graph-with-all");
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);

        Game gameEntity = entityManager.find(Game.class, gameId, properties);

        return gameEntity;

    }


    
}
