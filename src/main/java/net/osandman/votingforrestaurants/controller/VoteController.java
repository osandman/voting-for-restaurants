package net.osandman.votingforrestaurants.controller;

import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.dto.AuthUser;
import net.osandman.votingforrestaurants.dto.VoteTo;
import net.osandman.votingforrestaurants.entity.Vote;
import net.osandman.votingforrestaurants.repository.VoteRepository;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = VoteController.VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteController {
    public final static String VOTE_URL = "/profile/votes";
    private final VoteRepository voteRepository;

    @GetMapping("/{id}")
    public VoteTo getById(@PathVariable int id) {
        Vote vote = voteRepository.getExisted(id);
        return new VoteTo(vote.id(), vote.getMenu().getRestaurant().getId(), vote.getVoteDate());
    }

    @GetMapping
    public List<VoteTo> getAllByPersonId(@AuthenticationPrincipal AuthUser authUser) {
        List<Vote> votes = voteRepository.findAllByPersonId(authUser.getId());
        return votes.stream()
                .map(vote -> new VoteTo(vote.id(), vote.getMenu().getRestaurant().getId(), vote.getVoteDate()))
                .toList();
    }
}
