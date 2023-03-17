package site.nomoreparties.stellarburgers.tests.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseVO {
    Boolean success;

    // when success
    User user;
    String accessToken;
    String refreshToken;

    // when failure
    String message;
}
