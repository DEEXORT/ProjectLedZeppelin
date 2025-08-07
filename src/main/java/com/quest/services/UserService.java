package com.quest.services;

import com.quest.entity.User;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.repository.UserRepository;
import com.quest.util.MessageBundle;

import java.util.Collection;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    public void create(User user) throws UserAlreadyExistsException, UserEmptyException {
        validateCredentials(user.getLogin(), user.getPassword());
        // Проверка на уже существующего пользователя
        if (userRepository.find(user.getLogin()) != null) {
            throw new UserAlreadyExistsException(
                    MessageBundle.get("error.user_already_exists").formatted(user.getLogin())
            );
        }
        // Регистрация пользователя в БД
        userRepository.create(user);
    }

    public Optional<User> get(String login, String password) throws UserEmptyException, UserNotFoundException {
        validateCredentials(login, password);

        User user = userRepository.find(login);
        if (user == null) {
            throw new UserNotFoundException(MessageBundle.get("error.user_not_found"));
        }
        if (!user.getPassword().equals(password)) {
            return Optional.empty();
        } else {
            return Optional.of(user);
        }
    }

    public Optional<User> get(long id) {
        return Optional.ofNullable(userRepository.get(id));
    }

    public Optional<User> update(User user) {
        userRepository.update(user);
        return Optional.of(user);
    }

    private void validateCredentials(String login, String password) throws UserEmptyException {
        if (login == null || login.isEmpty()) {
            throw new UserEmptyException(MessageBundle.get("error.login_not_empty"));
        }
        if (password == null || password.isEmpty()) {
            throw new UserEmptyException(MessageBundle.get("error.password_not_empty"));
        }
    }
}
