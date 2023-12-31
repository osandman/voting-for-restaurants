package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v JOIN FETCH v.menu WHERE v.person.id=?1")
    List<Vote> findAllByPersonId(int id);

    @Query("SELECT v FROM Vote v JOIN FETCH v.menu WHERE v.person.id=?1 AND v.voteDate=?2")
    Optional<Vote> findByPersonIdAndVoteDate(int id, LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.menu WHERE v.id=?1")
    Optional<Vote> findById(int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.person.id=?1 AND v.voteDate=?2")
    int deleteByUserIdAndVoteDate(int id, LocalDate date);
}
