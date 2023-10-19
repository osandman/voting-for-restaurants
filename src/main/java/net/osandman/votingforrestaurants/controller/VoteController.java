package net.osandman.votingforrestaurants.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.dto.AuthUser;
import net.osandman.votingforrestaurants.dto.VoteTo;
import net.osandman.votingforrestaurants.entity.Menu;
import net.osandman.votingforrestaurants.entity.Vote;
import net.osandman.votingforrestaurants.error.NotFoundException;
import net.osandman.votingforrestaurants.error.TimeIsOverException;
import net.osandman.votingforrestaurants.repository.MenuRepository;
import net.osandman.votingforrestaurants.repository.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteController.VOTE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class VoteController {
    public static final String VOTE_URL = "/profile/votes";
    public static final LocalTime BOUNDARY_OF_TIME = LocalTime.of(11, 00);
    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    @GetMapping("/{id}")
    public VoteTo getById(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        Vote vote = voteRepository.getExisted(id);
        if (vote.getPerson().getId() != null && !vote.getPerson().getId().equals(authUser.getId())) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
        return new VoteTo(vote.id(), vote.getMenu().getRestaurant().getId(), vote.getVoteDate());
    }

    @GetMapping
    public List<VoteTo> getAllByPersonId(@AuthenticationPrincipal AuthUser authUser) {
        List<Vote> votes = voteRepository.findAllByPersonId(authUser.getId());
        return votes.stream()
                .map(vote -> new VoteTo(vote.id(), vote.getMenu().getRestaurant().getId(), vote.getVoteDate()))
                .toList();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public VoteTo create(@AuthenticationPrincipal AuthUser authUser,
                         @RequestParam("restaurantId") @NotNull int restaurantId) {
        Menu menu = menuRepository.findMenuByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException("Entity with id=" + restaurantId + " not found"));
        Vote vote = new Vote(menu, authUser.getPerson());
        voteRepository.save(vote);
        return new VoteTo(vote.id(), menu.getRestaurant().getId(), vote.getVoteDate());
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        if (LocalTime.now().isBefore(BOUNDARY_OF_TIME)) {
            if (voteRepository.deleteByUserIdAndVoteDate(authUser.getId(), LocalDate.now()) == 0) {
                throw new NotFoundException("Not found vote for current day");
            }
        } else {
            throw new TimeIsOverException("Can't delete vote, the time is over than " + BOUNDARY_OF_TIME);
        }
    }
}
