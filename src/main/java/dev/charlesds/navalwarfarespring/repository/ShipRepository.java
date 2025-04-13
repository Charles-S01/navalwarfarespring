package dev.charlesds.navalwarfarespring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.charlesds.navalwarfarespring.entity.Ship;

public interface ShipRepository extends JpaRepository<Ship, String> {

}


