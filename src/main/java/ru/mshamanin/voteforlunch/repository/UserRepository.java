package ru.mshamanin.voteforlunch.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.NotFoundException;
import ru.mshamanin.voteforlunch.model.User;

import java.util.List;
import java.util.Optional;

import static ru.mshamanin.voteforlunch.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    Sort SORT_EMAIL = Sort.by(Sort.Direction.ASC, "email");

    @Cacheable(cacheNames = "users", key = "'users'")
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Transactional
    @Modifying
    @CacheEvict(cacheNames = "users", key = "'users'")
    default User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return save(user);
    }

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }

    default List<User> getAll() {
        return findAll(SORT_EMAIL);
    }

    @CacheEvict(cacheNames = "users", key = "'users'")
    default void deleteUser(int id) {
        deleteExisted(id);
    }
}
