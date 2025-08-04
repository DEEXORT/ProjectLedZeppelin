package com.quest.services;

import com.quest.entity.User;
import com.quest.exception.UserAlreadyExistsException;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.repository.UserRepository;

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
            throw new UserAlreadyExistsException("Пользователь с логином " + user.getLogin() + " уже существует");
        }
        // Регистрация пользователя в БД
        userRepository.create(user);
    }

    public Optional<User> get(String login, String password) throws UserEmptyException, UserNotFoundException {
        validateCredentials(login, password);

        User user = userRepository.find(login);
        if (user == null) {
            throw new UserNotFoundException("Такого пользователя не существует");
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
            throw new UserEmptyException("Логин не может быть пустым");
        }
        if (password == null || password.isEmpty()) {
            throw new UserEmptyException("Пароль не может быть пустым");
        }
    }
}
