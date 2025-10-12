package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.entity.User;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserInvalidPasswordException;
import com.quest.exception.UserNotFoundException;
import com.quest.repository.RepositoryImpl;
import com.quest.util.KeyAttribute;
import com.quest.util.ResourceBundleManager;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.stream.Stream;

public class HibernateUserService extends AbstractBaseService<User> {

    public HibernateUserService() {
        super(ServiceLocator.getService(RepositoryImpl.class, User.class));
    }

    public HibernateUserService(RepositoryImpl<User> repository) {
        super(repository);
    }

    @Override
    public void create(User user) throws UserAlreadyExistsException, UserEmptyException {
        validateCredentials(user.getLogin(), user.getPassword());
        // Проверка на уже существующего пользователя
        Stream<User> stream = repository.find(user);
        if (stream.findFirst().isPresent()) {
            throw new UserAlreadyExistsException(
                    ResourceBundleManager.getMessage("error.user_already_exists").formatted(user.getLogin())
            );
        }
        // Регистрация пользователя в БД
        repository.create(user);
    }

    public boolean isGuest(HttpServletRequest req) {
        return req.getSession().getAttribute(KeyAttribute.USER) == null;
    }

    private void validateCredentials(String login, String password) throws UserEmptyException {
        if (login == null || login.isEmpty()) {
            throw new UserEmptyException(ResourceBundleManager.getMessage("error.login_not_empty"));
        }
        if (password == null || password.isEmpty()) {
            throw new UserEmptyException(ResourceBundleManager.getMessage("error.password_not_empty"));
        }
    }

    public Optional<User> get(String login, String password) throws UserEmptyException, UserNotFoundException, UserInvalidPasswordException {
        validateCredentials(login, password);

        User patternUser = User.builder()
                .login(login)
                .password(password)
                .build();

        Optional<User> userOptional = repository.find(patternUser).findFirst();
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(ResourceBundleManager.getMessage("error.user_not_found"));
        } else {
            User user = userOptional.get();
            if (!user.getPassword().equals(password)) {
                throw new UserInvalidPasswordException(ResourceBundleManager.getMessage("error.invalid_credentials"));
            } else {
                return Optional.of(user);
            }
        }
    }
}
