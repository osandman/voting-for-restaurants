package net.osandman.votingforrestaurants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.osandman.votingforrestaurants.entity.HasId;
import org.springframework.lang.Nullable;

public record RestaurantTo(
        @Nullable
        Integer id,
        @NotBlank
        @Size(min = 2, max = 100)
        String name,
        @NotBlank
        @Size(max = 100)
        String address) implements HasId {
}