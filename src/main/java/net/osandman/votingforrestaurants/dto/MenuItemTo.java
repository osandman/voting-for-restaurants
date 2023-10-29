package net.osandman.votingforrestaurants.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record MenuItemTo(
        @NotNull
        @JsonProperty("dishId")
        Integer dishId,
        @NotNull
        @PositiveOrZero
        BigDecimal amount
) {
}
