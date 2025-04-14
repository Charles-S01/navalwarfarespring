package dev.charlesds.navalwarfarespring.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Gameboard {
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JsonIgnore
    private Player player;

    @Builder.Default
    @OneToMany(mappedBy = "gameboard", cascade = CascadeType.ALL)
    private List<Ship> ships = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "gameboard", cascade = CascadeType.ALL)
    private List<Coordinate> coordinates = new ArrayList<>();

    public void addCoordinate(Coordinate coordinate) {
        coordinates.add(coordinate);
        coordinate.setGameboard(this);
    }

    public void addShip(Ship ship) {
        ships.add(ship);
        ship.setGameboard(this);
    }

}
