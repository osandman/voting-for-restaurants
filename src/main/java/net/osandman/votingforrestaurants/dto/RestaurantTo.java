package net.osandman.votingforrestaurants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantTo(Integer id,
                           @NotBlank
                           @Size(min = 2, max = 100)
                           String name,
                           @NotBlank
                           @Size(max = 100)
                           String address) {
}