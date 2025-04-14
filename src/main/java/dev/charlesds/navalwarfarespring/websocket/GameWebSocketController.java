package dev.charlesds.navalwarfarespring.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ch.qos.logback.core.spi.ConfigurationEvent.EventType;
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
import dev.charlesds.navalwarfarespring.response.Event;
import dev.charlesds.navalwarfarespring.response.GameUpdate;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Controller
public class GameWebSocketController {

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
    
    @Transactional
    @MessageMapping("/attack")
    public void attack(@Payload Map<String, Object> body) {
        System.out.println("attack");
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

    private Game getGameEntity(String gameId) {
        EntityGraph entityGraph = entityManager.getEntityGraph("game-entity-graph-with-all");
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);

        Game gameEntity = entityManager.find(Game.class, gameId, properties);

        return gameEntity;

    }

}
