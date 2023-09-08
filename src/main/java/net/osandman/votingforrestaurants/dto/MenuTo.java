package net.osandman.votingforrestaurants.dto;

import net.osandman.votingforrestaurants.entity.HasId;
import org.springframework.lang.Nullable;

import java.util.List;

public record MenuTo(
        @Nullable
        Integer id,
        @Nullable
        List<MenuItemTo> menuItems
) implements HasId {
}
