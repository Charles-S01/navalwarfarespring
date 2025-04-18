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
import dev.charlesds.navalwarfarespring.service.GameService;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Controller
public class GameWebSocketController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GameService gameService;
    
    @MessageMapping("/attack")
    public void attack(@Payload Map<String, Object> body) {
        System.out.println("/attack");
        gameService.handleAttack(body);
    }

}
