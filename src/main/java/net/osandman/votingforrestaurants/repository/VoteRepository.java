package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.person.id=?1")
    List<Vote> findAllByPersonId(int id);
}
