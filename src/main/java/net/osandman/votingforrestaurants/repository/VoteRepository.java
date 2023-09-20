package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.person.id=?1")
    List<Vote> findAllByPersonId(int id);

    @Query("SELECT v FROM Vote v WHERE v.person.id=?1 AND v.voteDate=CURRENT_DATE")
    Optional<Vote> findByIdCurrentDay(int id);
}
