package net.osandman.votingforrestaurants.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.osandman.votingforrestaurants.entity.HasId;
import net.osandman.votingforrestaurants.entity.RoleType;
import net.osandman.votingforrestaurants.util.JsonDeserializers;
import org.springframework.lang.Nullable;

import java.util.Set;

public record PersonTo(
        @Nullable
        Integer id,
        @NotBlank
        @Size(min = 2, max = 100)
        String name,
        @NotBlank
        @Email
        @Size(max = 100)
        String email,
        @Size(max = 100)
        @Nullable
        @JsonDeserialize(using = JsonDeserializers.PasswordDeserializer.class)
        String password,
        @Nullable
        Set<RoleType> roleTypes) implements HasId {
}
