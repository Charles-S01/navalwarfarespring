package dev.charlesds.navalwarfarespring.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"gameboard_id", "row", "col"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Coordinate {
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JsonIgnore
    private Gameboard gameboard;

    @ManyToOne
    private Ship ship;

    private int row;
    private int col;
    @Enumerated(EnumType.STRING)
    private AttackResult attackResult = null;
    // @Enumerated(EnumType.STRING)
    // private ShipType shipType = null;

}

