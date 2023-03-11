package praktikum;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.model.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@Story("Get User Orders")
public class GetUserOrdersTest extends ApiTest {

    IngredientsResponseVO ingredients;
    List<String> ingredientIds;

    @Before
    public void generateUserData() {
        ingredients = getIngredients();
        ingredientIds = ingredients.getIngredientList().stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());
        super.generateUserData();
        createUser(email, password, name);
        createOrder(accessToken, List.of(ingredientIds.get(0), ingredientIds.get(1)));
    }

    @After
    public void deleteUser() {
        super.deleteUser();
    }

    @Test
    @DisplayName("unauthorized get attempt")
    public void unauthorizedGetAttempt() {
        // given
        GetUserOrdersResponseVO getUserOrdersResponseVO =
                getUserOrders("", false, SC_UNAUTHORIZED);

        // then
        assertThat(getUserOrdersResponseVO.getMessage())
                .isEqualTo("You should be authorised");
    }

    @Test
    @DisplayName("get")
    public void get() {
        // given
        GetUserOrdersResponseVO response = getUserOrders(accessToken);

        // then
        assertThat(response.getOrders().size()).isOne();
        OrderGetResultVO order = response.getOrders().get(0);
        assertThat(order.getId()).isNotBlank();
        assertThat(order.getIngredientIds())
                .isEqualTo(List.of(ingredientIds.get(0), ingredientIds.get(1)));

        assertThat(response.getTotal()).isGreaterThan(0);
        assertThat(response.getTotalToday()).isGreaterThan(0);
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
                List.of(ingredientIds.get(0) + "1234"),
                SC_INTERNAL_SERVER_ERROR);
    }
}
