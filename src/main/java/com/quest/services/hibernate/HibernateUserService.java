package com.quest.services.hibernate;

import com.quest.config.ServiceLocator;
import com.quest.dto.UserTo;
import com.quest.entity.User;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserInvalidPasswordException;
import com.quest.exception.UserNotFoundException;
import com.quest.mapping.Dto;
import com.quest.repository.RepositoryImpl;
import com.quest.util.KeyAttribute;
import com.quest.util.ResourceBundleManager;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HibernateUserService {
    private final RepositoryImpl<User> repository;
    private Dto dto = Dto.MAPPER;

    public HibernateUserService() {
        this.repository = ServiceLocator.getService(RepositoryImpl.class, User.class);
    }

    public HibernateUserService(RepositoryImpl<User> repository) {
        this.repository = repository;
    }

    public Optional<UserTo> get(long id) {
        return Optional.ofNullable(repository.get(id)).map(dto::from);
    }

    public void update(UserTo userTo) {
        repository.update(dto.from(userTo));
    }

    public void create(UserTo userTo) throws UserAlreadyExistsException, UserEmptyException {
        validateCredentials(userTo.getLogin(), userTo.getPassword());
        // Проверка на уже существующего пользователя
        Stream<User> stream = repository.find(dto.from(userTo));
        if (stream.findFirst().isPresent()) {
            throw new UserAlreadyExistsException(
                    ResourceBundleManager.getMessage("error.user_already_exists").formatted(userTo.getLogin())
            );
        }
        // Регистрация пользователя в БД
        repository.create(dto.from(userTo));
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

    public Optional<UserTo> get(String login, String password) throws UserEmptyException, UserNotFoundException, UserInvalidPasswordException {
        validateCredentials(login, password);

        User patternUser = User.builder()
                .login(login)
                .password(password)
                .build();

        Optional<UserTo> optUserTo = repository.find(patternUser).map(dto::from).findFirst();
        if (optUserTo.isEmpty()) {
            throw new UserNotFoundException(ResourceBundleManager.getMessage("error.user_not_found"));
        } else {
            UserTo user = optUserTo.get();
            if (!user.getPassword().equals(password)) {
                throw new UserInvalidPasswordException(ResourceBundleManager.getMessage("error.invalid_credentials"));
            } else {
                return Optional.of(user);
            }
        }
    }

    public Collection<UserTo> getAll() {
        return repository.getAll().stream().map(dto::from).collect(Collectors.toList());
    }

    public void delete(UserTo user) {
        repository.delete(dto.from(user));
    }
}
