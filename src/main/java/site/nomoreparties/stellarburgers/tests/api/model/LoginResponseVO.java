package site.nomoreparties.stellarburgers.tests.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVO {
    Boolean success;

    // when success
    String accessToken;
    String refreshToken;
    User user;

    // when failure
    String message;
}
