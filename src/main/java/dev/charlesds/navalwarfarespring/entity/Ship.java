package dev.charlesds.navalwarfarespring.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"gameboard_id", "shipType"}))

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Ship {
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JsonIgnore
    private Gameboard gameboard;

    private int length;
    @Enumerated(EnumType.STRING)
    private ShipType shipType = ShipType.CARRIER;
    
    @OneToMany
    @JsonIgnore
    private List<Coordinate> coordinates;
}
