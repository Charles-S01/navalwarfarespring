package dev.charlesds.navalwarfarespring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.charlesds.navalwarfarespring.entity.Coordinate;


public interface CoordinateRepository extends JpaRepository<Coordinate, String> {

}

