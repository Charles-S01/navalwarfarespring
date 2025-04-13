package dev.charlesds.navalwarfarespring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.charlesds.navalwarfarespring.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {

}

