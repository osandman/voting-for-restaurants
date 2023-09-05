package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Role;
import net.osandman.votingforrestaurants.entity.RoleType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Transactional(readOnly = true)
public interface RoleRepository extends BaseRepository<Role> {
    Optional<Role> findByType(RoleType type);

    Set<Role> findAllByTypeIn(Set<RoleType> roleTypes);
}
