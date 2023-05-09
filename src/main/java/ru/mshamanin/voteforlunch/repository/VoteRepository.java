package ru.mshamanin.voteforlunch.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.DataConflictException;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date")
    List<Vote> getAll(int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> get(int id, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user WHERE v.date = ?1 and v.user.id = ?2")
    Optional<Vote> findByDate(LocalDate date, int userId);
}