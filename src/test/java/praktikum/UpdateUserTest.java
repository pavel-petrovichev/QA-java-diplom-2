package praktikum;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.*;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("FieldMayBeFinal")
@Story("Update User")
public class UpdateUserTest extends ApiTest {

    @Before
    public void generateUserData() {
        super.generateUserData();
    }

    @After
    public void deleteUser() {
        super.deleteUser();
    }

    @Test
    @DisplayName("unauthorized update attempt")
    public void unauthorizedUpdateAttempt() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newEmail = faker.internet().emailAddress();
        String newName = faker.name().name();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        "",
                        UpdateUserRequestVO.builder()
                                .email(newEmail)
                                .name(newName)
                                .build(),
                        false,
                        SC_UNAUTHORIZED);

        // then
        assertThat(updateUserResponseVO.getMessage())
                .isEqualTo("You should be authorised");
    }

    @Test
    @DisplayName("update email response")
    public void updateEmailResponse() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newEmail = faker.internet().emailAddress();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String email = newEmail;
                        });

        // then
        assertThat(updateUserResponseVO.getUser())
                .isEqualTo(User.of(newEmail, name));
    }

    @Test
    @DisplayName("user data after email update")
    public void userDataAfterEmailUpdate() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newEmail = faker.internet().emailAddress();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String email = newEmail;
                        });

        // then
        GetUserResponseVO getUserResponseVO = getUser(loginResponseVO.getAccessToken(), null);
        assertThat(getUserResponseVO.getUser())
                .isEqualTo(User.of(newEmail, name));
    }

    @Test
    @DisplayName("update name response")
    public void updateNameResponse() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newName = faker.name().name();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String name = newName;
                        });

        // then
        assertThat(updateUserResponseVO.getUser())
                .isEqualTo(User.of(email, newName));
    }

    @Test
    @DisplayName("user data after name update")
    public void userDataAfterNameUpdate() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newName = faker.name().name();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String name = newName;
                        });

        // then
        GetUserResponseVO getUserResponseVO = getUser(loginResponseVO.getAccessToken(), null);
        assertThat(getUserResponseVO.getUser())
                .isEqualTo(User.of(email, newName));
    }

    @Test
    @DisplayName("update email and name response")
    public void updateEmailAndNameResponse() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newEmail = faker.internet().emailAddress();
        String newName = faker.name().name();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String email = newEmail;
                            public String name = newName;
                        });

        // then
        assertThat(updateUserResponseVO.getUser())
                .isEqualTo(User.of(newEmail, newName));
    }

    @Test
    @DisplayName("user data after email and name update")
    public void userDataAfterEmailAndNameUpdate() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newEmail = faker.internet().emailAddress();
        String newName = faker.name().name();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String email = newEmail;
                            public String name = newName;
                        });

        // then
        GetUserResponseVO getUserResponseVO = getUser(loginResponseVO.getAccessToken(), null);
        assertThat(getUserResponseVO.getUser())
                .isEqualTo(User.of(newEmail, newName));
    }

    @Test
    @DisplayName("null email should be ignored")
    public void nullEmailShouldBeIgnored() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newName = faker.name().name();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String email = null;
                            public String name = newName;
                        });

        // then
        assertThat(updateUserResponseVO.getUser())
                .isEqualTo(User.of(email, newName));
    }

    @Test
    @DisplayName("null name should be ignored")
    public void nullNameShouldBeIgnored() {
        // given
        LoginResponseVO loginResponseVO = createUser();

        // when
        String newEmail = faker.internet().emailAddress();
        UpdateUserResponseVO updateUserResponseVO =
                updateUser(
                        loginResponseVO.getAccessToken(),
                        new Object() {
                            public String email = newEmail;
                            public String name = null;
                        });

        // then
        assertThat(updateUserResponseVO.getUser())
                .isEqualTo(User.of(newEmail, name));
    }

    private LoginResponseVO createUser() {
        CreateUserResponseVO createUserResponseVO = createUser(email, password, name);
        assertThat(createUserResponseVO.getUser())
                .isEqualTo(User.of(email, name));
        return login(email, password, accessToken);
    }
}
