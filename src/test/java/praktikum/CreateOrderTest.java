package praktikum;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.CreateOrderResponseVO;
import praktikum.model.Ingredient;
import praktikum.model.IngredientsResponseVO;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("FieldMayBeFinal")
@Story("Create Order")
public class CreateOrderTest extends ApiTest {

    IngredientsResponseVO ingredients;
    List<String> ids;

    @Before
    public void generateUserData() {
        ingredients = getIngredients();
        ids = ingredients.getIngredientList().stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
        super.generateUserData();
        createUser(email, password, name);
    }

    @After
    public void deleteUser() {
        super.deleteUser();
    }

    @Test
    @DisplayName("unauthorized create attempt")
    public void unauthorizedCreateAttempt() {
        // given
        CreateOrderResponseVO orderResponseVO =
                createOrder("", List.of(ids.get(0), ids.get(1)), false, SC_UNAUTHORIZED);

        // then
        assertThat(orderResponseVO.getMessage())
                .isEqualTo("You should be authorised");
    }

    @Test
    @DisplayName("create")
    public void create() {
        // given
        CreateOrderResponseVO orderResponseVO = createOrder(accessToken, List.of(ids.get(0), ids.get(1)));

        // then
        List<String> orderIds = orderResponseVO.getOrder().getIngredients().stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
        assertThat(orderIds)
                .contains(ids.get(0), ids.get(1));
    }

    @Test
    @DisplayName("no ingredients")
    public void noIngredients() {
        // given
        CreateOrderResponseVO orderResponseVO =
                createOrder(accessToken, List.of(), false, SC_BAD_REQUEST);

        // then
        assertThat(orderResponseVO.getMessage())
                .isEqualTo("Ingredient ids must be provided");
    }

    @Test
    @DisplayName("invalid ingredient")
    public void invalidIngredient() {
        createOrderWithUnknownIngredients(
                accessToken,
                List.of(ids.get(0) + "1234"),
                SC_INTERNAL_SERVER_ERROR);
    }
}
