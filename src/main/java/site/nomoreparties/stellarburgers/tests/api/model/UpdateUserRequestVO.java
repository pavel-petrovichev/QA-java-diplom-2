package site.nomoreparties.stellarburgers.tests.api.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UpdateUserRequestVO {
    String email;
    String name;
}
