package site.nomoreparties.stellarburgers.tests.api;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.tests.api.model.CreateUserResponseVO;
import site.nomoreparties.stellarburgers.tests.api.model.User;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Create User")
public class CreateUserTest extends ApiTest {

    @Before
    public void generateUserData() {
        super.generateUserData();
    }

    @After
    public void deleteUser() {
        super.deleteUser();
    }

    @Test
    @DisplayName("create")
    public void create() {
        CreateUserResponseVO response = createUser(email, password, name);

        assertThat(response.getUser())
                .isEqualTo(User.of(email, name));
    }

    @Test
    @DisplayName("user already exists")
    public void alreadyExistingUser() {
        createUser(email, password, name);

        CreateUserResponseVO failure = createUser(email, password, name, false, SC_FORBIDDEN);
        assertThat(failure.getMessage())
                .isEqualTo("User already exists");
    }

    @Test
    @Tag("field-presence")
    @DisplayName("mandatory field: email")
    public void mandatoryFieldEmail() {
        CreateUserResponseVO failure = createUser(null, password, name, false, SC_FORBIDDEN);
        assertThat(failure.getMessage())
                .isEqualTo("Email, password and name are required fields");
    }

    @Test
    @Tag("field-presence")
    @DisplayName("mandatory field: password")
    public void mandatoryFieldPassword() {
        CreateUserResponseVO failure = createUser(email, null, name, false, SC_FORBIDDEN);
        assertThat(failure.getMessage())
                .isEqualTo("Email, password and name are required fields");
    }

    @Test
    @Tag("field-presence")
    @DisplayName("mandatory field: name")
    public void mandatoryFieldName() {
        CreateUserResponseVO failure = createUser(email, password, null, false, SC_FORBIDDEN);
        assertThat(failure.getMessage())
                .isEqualTo("Email, password and name are required fields");
    }
}
