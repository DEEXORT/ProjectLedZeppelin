package services;

import com.quest.entity.User;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.quest.repository.UserRepository;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Test
    void create() {

    }

    @Test
    void get_shouldReturnUserWithValidCredentials() throws UserNotFoundException {
        // given
        UserService userService = new UserService(userRepository);
        User user = User.builder()
                .id(1L)
                .login("admin")
                .password("admin")
                .build();
        Mockito.doReturn(user).when(userRepository).find("admin");

        // when
        Optional<User> optionalUser = userService.get("admin", "admin");

        // when + then
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(user.getLogin(), optionalUser.get().getLogin());
    }

    @Test
    void get_shouldThrowExceptionForInvalidCredentials() {
        // given
        UserService userService = new UserService(userRepository);
        Mockito.doReturn(null).when(userRepository).find("incorrectLogin");

        // when + then
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.get("incorrectLogin", "incorrectPassword"));
    }

    @Test
    void get_shouldReturnExceptionForNullCredentials() {
        // given
        UserService userService = new UserService(userRepository);

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