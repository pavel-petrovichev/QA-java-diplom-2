package praktikum.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class LoginRequestVO {
    String email;
    String password;
}
