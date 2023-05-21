package ru.mshamanin.voteforlunch.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.DataConflictException;
import ru.mshamanin.voteforlunch.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.user.id=:userId ORDER BY v.date")
    List<Vote> getAll(int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> getWithoutRestaurant(int id, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> get(int id, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.date = ?1 and v.user.id = ?2")
    Optional<Vote> findByDate(LocalDate date, int userId);

    default void getExistedOrBelonged(int userId, int id) {
        getWithoutRestaurant(id, userId).orElseThrow(() -> new DataConflictException(
                "Vote id=" + id + "  is not exist or doesn't belong to User id=" + userId
        ));
    }
}