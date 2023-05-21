package ru.mshamanin.voteforlunch.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.mshamanin.voteforlunch.model.User;
import ru.mshamanin.voteforlunch.repository.UserRepository;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    protected UserRepository userRepository;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public User get(int id) {
        log.info("get {}", id);
        return userRepository.getExisted(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userRepository.deleteUser(id);
    }
}