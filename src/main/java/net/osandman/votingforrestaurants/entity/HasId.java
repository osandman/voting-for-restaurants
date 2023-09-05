package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasId {
    Integer id();

    @JsonIgnore
    default boolean isNew() {
        return id() == null;
    }
}
