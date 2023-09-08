package net.osandman.votingforrestaurants.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import net.osandman.votingforrestaurants.entity.HasId;

import java.math.BigDecimal;

public record MenuItemTo(
        @NotNull
        @JsonProperty("dishId")
        Integer id, // means dish id
        @NotNull
        BigDecimal amount
) implements HasId {
}
