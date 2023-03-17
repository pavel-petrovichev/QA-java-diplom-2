package site.nomoreparties.stellarburgers.tests.api.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateUserRequestVO {
    String email;
    String password;
    String name;
}
