package net.osandman.votingforrestaurants.dto;

import jakarta.validation.Valid;
import net.osandman.votingforrestaurants.entity.HasId;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;

public record MenuTo(
        @Nullable
        Integer id,
        @Nullable
        LocalDate date,
        @Nullable
        List<@Valid MenuItemTo> menuItems
) implements HasId {
}
