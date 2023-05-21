package ru.mshamanin.voteforlunch.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.DataConflictException;
import ru.mshamanin.voteforlunch.error.IllegalRequestDataException;
import ru.mshamanin.voteforlunch.model.Vote;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;
import ru.mshamanin.voteforlunch.repository.UserRepository;
import ru.mshamanin.voteforlunch.repository.VoteRepository;
import ru.mshamanin.voteforlunch.util.DateTimeUtil;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class VoteService {
    public static final String TOO_LATE_TO_VOTE_ERROR = "It's too late to vote for restaurant.";
    public static final String CHANGE_NOT_TODAY_VOTE_ERROR = "To change your not today vote is prohibited.";
    public static final String SECOND_VOTE_CREATION_ERROR = "To create second vote for today is prohibited.";

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public Vote create(int restaurantId, int userId) {
        Vote vote = voteRepository.findByDate(DateTimeUtil.getCurrentDate(), userId).orElseGet(() ->
                new Vote(null, DateTimeUtil.getCurrentDate(), null, userRepository.getReferenceById(userId)));
        if (!vote.isNew()) {
            throw new DataConflictException(SECOND_VOTE_CREATION_ERROR);
        } else {
            vote.setRestaurant(restaurantRepository.getExisted(restaurantId));
            return voteRepository.save(vote);
        }
    }

    @Transactional
    public void update(Vote vote) {
        if (!DateTimeUtil.inTime()) {
            throw new IllegalRequestDataException(TOO_LATE_TO_VOTE_ERROR);
        } else if (!vote.getDate().isEqual(DateTimeUtil.getCurrentDate())) {
            throw new IllegalRequestDataException(CHANGE_NOT_TODAY_VOTE_ERROR);
        } else voteRepository.save(vote);
    }
}