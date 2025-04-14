package dev.charlesds.navalwarfarespring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.charlesds.navalwarfarespring.entity.Coordinate;


public interface CoordinateRepository extends JpaRepository<Coordinate, String> {

    Optional<Coordinate> findByGameboardIdAndRowAndCol(String gameboardId, int row, int col);

}

