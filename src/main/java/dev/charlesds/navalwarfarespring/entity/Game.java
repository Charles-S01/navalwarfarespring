package dev.charlesds.navalwarfarespring.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NamedEntityGraph(
  name = "game-entity-graph-with-all",
  attributeNodes = {
    @NamedAttributeNode("id"),
    @NamedAttributeNode("playerTurnId"),
    @NamedAttributeNode("winnerId"),
    @NamedAttributeNode(value = "players", subgraph = "players-subgraph"),
  },
  subgraphs = {
    @NamedSubgraph(
      name = "players-subgraph",
      attributeNodes = {
        @NamedAttributeNode("id"),
        @NamedAttributeNode(value = "gameboard", subgraph = "gameboard-subgraph"),
      }
    ),
    @NamedSubgraph(
        name = "gameboard-subgraph",
        attributeNodes = {
            @NamedAttributeNode("id"),
            @NamedAttributeNode(value = "ships", subgraph = "ships-subgraph"),
            @NamedAttributeNode(value = "coordinates", subgraph = "coordinates-subgraph"),
        }
    ),
    @NamedSubgraph(
        name = "ships-subgraph",
        attributeNodes = {
            @NamedAttributeNode("id"),
            @NamedAttributeNode("length"),
            @NamedAttributeNode("shipType"),
        }
    ),
    @NamedSubgraph(
        name = "coordinates-subgraph",
        attributeNodes = {
            @NamedAttributeNode("id"),
            @NamedAttributeNode("row"),
            @NamedAttributeNode("col"),
            @NamedAttributeNode("ship"),
            @NamedAttributeNode("attackResult"),
        }
    )
  }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Game {
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id = UUID.randomUUID().toString();

    private String playerTurnId;
    private String winnerId;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player) {
        player.setGame(this);
        players.add(player);
    }
}
