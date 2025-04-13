package dev.charlesds.navalwarfarespring.request;

import java.util.List;

import dev.charlesds.navalwarfarespring.entity.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceShipRequest {
    private String gameId;
    private String playerId;
    private List<Coordinate> coords;
}
