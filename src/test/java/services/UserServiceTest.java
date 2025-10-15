package services;

import com.quest.dto.UserTo;
import com.quest.entity.User;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.repository.RepositoryImpl;
import com.quest.services.hibernate.HibernateUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    RepositoryImpl<User> userRepository;

    @Test
    void create() {

    }

    @Test
    void get_shouldReturnUserWithValidCredentials() throws UserNotFoundException {
        // given
        HibernateUserService userService = new HibernateUserService(userRepository);
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        Mockito.doReturn(user).when(userRepository).find(user);

        // when
        Optional<UserTo> optionalUser = userService.get("admin", "admin");

        // when + then
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(user.getLogin(), optionalUser.get().getLogin());
    }

    @Test
    void get_shouldThrowExceptionForInvalidCredentials() {
        // given
        HibernateUserService userService = new HibernateUserService(userRepository);
        User user = User.builder()
                .login("incorrect")
                .password("incorrect")
                .build();
        Mockito.doReturn(new ArrayList<>().stream()).when(userRepository).find(user);

        // when + then
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.get("incorrectLogin", "incorrectPassword"));
    }

    @Test
    void get_shouldReturnExceptionForNullCredentials() {
        // given
        HibernateUserService userService = new HibernateUserService(userRepository);

        // when + then
        Assertions.assertThrows(UserEmptyException.class, () -> {
            userService.get("admin", null);
        });
        Assertions.assertThrows(UserEmptyException.class, () -> {
            userService.get("admin", "");
        });
        Assertions.assertThrows(UserEmptyException.class, () -> {
            userService.get("", "admin");
        });
        Assertions.assertThrows(UserEmptyException.class, () -> {
            userService.get(null, "admin");
        });
        Assertions.assertThrows(UserEmptyException.class, () -> {
            userService.get(null, null);
        });
    }
}