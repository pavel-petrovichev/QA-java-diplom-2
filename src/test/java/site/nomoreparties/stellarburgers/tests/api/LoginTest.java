package site.nomoreparties.stellarburgers.tests.api;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.tests.api.model.CreateUserResponseVO;
import site.nomoreparties.stellarburgers.tests.api.model.LoginResponseVO;
import site.nomoreparties.stellarburgers.tests.api.model.User;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Login")
public class LoginTest extends ApiTest {

    @Before
    public void generateUserData() {
        super.generateUserData();
    }

    @After
    public void deleteUser() {
        super.deleteUser();
    }

    @Test
    @DisplayName("login")
    public void login() {
        // given
        createUser(email, password, name);

        // when
        LoginResponseVO response = login(email, password);

        // then
        assertThat(response.getUser())
                .isEqualTo(User.of(email, name));
    }

    @Test
    @DisplayName("non existing user login")
    public void nonExistingUserLogin() {
        // when
        LoginResponseVO response = login(email, password, false, SC_UNAUTHORIZED);

        // then
        assertThat(response.getMessage())
                .isEqualTo("email or password are incorrect");
    }

    @Test
    @DisplayName("login deleted user")
    public void loginDeletedUser() {
        // given
        createUser(email, password, name);

        // when
        login(email, password);
        deleteUser(accessToken);
        accessToken = null;
        LoginResponseVO response = login(email, password, false, SC_UNAUTHORIZED);

        // then
        assertThat(response.getMessage())
                .isEqualTo("email or password are incorrect");
    }

    @Test
    @Tag("field-presence")
    @DisplayName("mandatory field: email")
    public void mandatoryFieldEmail() {
        // given
        createUser(email, password, name);

        // when
        LoginResponseVO response = login(null, password, false, SC_UNAUTHORIZED);

        // then
        assertThat(response.getMessage())
                .isEqualTo("email or password are incorrect");
    }

    @Test
    @Tag("field-presence")
    @DisplayName("mandatory field: password")
    public void mandatoryFieldPassword() {
        // given
        createUser(email, password, name);

        // when
        LoginResponseVO response = login(email, null, false, SC_UNAUTHORIZED);

        // then
        assertThat(response.getMessage())
                .isEqualTo("email or password are incorrect");
    }
}
