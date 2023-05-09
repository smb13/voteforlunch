package ru.mshamanin.voteforlunch.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.IllegalRequestDataException;
import ru.mshamanin.voteforlunch.model.Vote;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;
import ru.mshamanin.voteforlunch.repository.VoteRepository;
import ru.mshamanin.voteforlunch.repository.UserRepository;
import ru.mshamanin.voteforlunch.util.DateTimeUtil;

@Service
@AllArgsConstructor
public class VoteService {
    public static final String TOO_LATE_TO_VOTE_ERROR = "It's too late to vote for restaurant.";

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository repository;

    @Transactional
    public Vote save(int restaurantId, int userId) {
        if (!DateTimeUtil.inTime()) {
            throw new IllegalRequestDataException(TOO_LATE_TO_VOTE_ERROR);
        }
        Vote vote = repository.findByDate(DateTimeUtil.getCurrentDate(), userId).orElseGet(() ->
                new Vote(null, DateTimeUtil.getCurrentDate(), null, userRepository.getExisted(userId)));
        vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return repository.save(vote);
    }
}