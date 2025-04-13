package dev.charlesds.navalwarfarespring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.charlesds.navalwarfarespring.entity.Game;


public interface GameRepository extends JpaRepository<Game, String> {

}
