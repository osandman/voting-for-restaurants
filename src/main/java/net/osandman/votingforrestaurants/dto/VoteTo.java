package net.osandman.votingforrestaurants.dto;

import net.osandman.votingforrestaurants.entity.HasId;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public record VoteTo(
        @Nullable
        Integer id,
        Integer restaurantId,
        LocalDate date
) implements HasId {
}
