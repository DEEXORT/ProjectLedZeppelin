package services;

import com.quest.config.ServiceLocator;
import com.quest.dto.UserTo;
import com.quest.entity.User;
import com.quest.exception.UserEmptyException;
import com.quest.exception.UserNotFoundException;
import com.quest.repository.RepositoryImpl;
import com.quest.services.hibernate.HibernateUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private MockedStatic<ServiceLocator> serviceLocator;

    @Mock
    private RepositoryImpl<User> userRepository;

    private HibernateUserService userService;

    @BeforeEach
    void setUp() {
        serviceLocator = Mockito.mockStatic(ServiceLocator.class);
        serviceLocator
                .when(() -> ServiceLocator.getService(RepositoryImpl.class, User.class))
                .thenReturn(userRepository);
        userService = new HibernateUserService();
    }

    @AfterEach
    void tearDown() {
        serviceLocator.close();
    }

    @Test
    void create() {

    }

    @Test
    void get_shouldReturnUserWithValidCredentials() throws UserNotFoundException {
        // given
        User user = User.builder()
                .login("admin")
                .password("admin")
                .build();
        Mockito.doReturn(Stream.of(user)).when(userRepository).find(any(User.class));

        // when
        Optional<UserTo> optionalUser = userService.get("admin", "admin");

        // when + then
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(user.getLogin(), optionalUser.get().getLogin());
    }

    @Test
    void get_shouldThrowExceptionForInvalidCredentials() {
        // given
        Mockito.doReturn(new ArrayList<>().stream()).when(userRepository).find(any(User.class));

        // when + then
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.get("incorrectLogin", "incorrectPassword"));
    }

    @Test
    void get_shouldReturnExceptionForNullCredentials() {
        // given + when + then
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