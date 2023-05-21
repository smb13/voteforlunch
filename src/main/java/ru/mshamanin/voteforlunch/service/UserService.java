package ru.mshamanin.voteforlunch.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.mshamanin.voteforlunch.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @CacheEvict(cacheNames = "users", key = "'users'")
    public void delete(int id) {
        userRepository.deleteExisted(id);
    }
}
