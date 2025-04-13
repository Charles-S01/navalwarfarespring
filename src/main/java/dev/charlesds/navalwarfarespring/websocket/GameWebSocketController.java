package dev.charlesds.navalwarfarespring.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import ch.qos.logback.core.spi.ConfigurationEvent.EventType;
import dev.charlesds.navalwarfarespring.entity.Game;
import dev.charlesds.navalwarfarespring.entity.Gameboard;
import dev.charlesds.navalwarfarespring.entity.Player;
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
    
    @MessageMapping("/attack")
    public void attack(@Payload Map<String, Object> obj) {


    }

    private Game getGameEntity(String gameId) {
        EntityGraph entityGraph = entityManager.getEntityGraph("game-entity-graph-with-all");
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);

        Game gameEntity = entityManager.find(Game.class, gameId, properties);

        return gameEntity;

    }

}
