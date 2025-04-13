package dev.charlesds.navalwarfarespring.response;

import dev.charlesds.navalwarfarespring.entity.Game;
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
public class GameUpdate { 
   private Event event; 
   private Game game; 
}