package net.osandman.votingforrestaurants.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import net.osandman.votingforrestaurants.entity.HasId;
import net.osandman.votingforrestaurants.error.IllegalRequestDataException;

import java.util.Objects;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(@NotNull HasId entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(@NotNull HasId entity, Integer id) {
        if (entity.isNew()) {
            return;
        }
        if (!Objects.equals(entity.id(), id)) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must has id=" + id);
        }
    }
}