package dev.charlesds.navalwarfarespring.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Player {
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne()
    @JsonIgnore
    private Game game;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private Gameboard gameboard;

    public void addGameboard(Gameboard gameboard) {
        this.gameboard = gameboard;
        gameboard.setPlayer(this);
    }
}
