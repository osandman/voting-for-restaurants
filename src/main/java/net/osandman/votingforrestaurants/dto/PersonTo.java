package net.osandman.votingforrestaurants.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.osandman.votingforrestaurants.util.JsonDeserializers;

public record PersonTo(Integer id,
                       @NotBlank
                       @Size(min = 2, max = 100)
                       String name,
                       @NotBlank
                       @Email
                       @Size(max = 100)
                       String email,
                       @Size(max = 100)
                       @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
                       String password
) {
}
