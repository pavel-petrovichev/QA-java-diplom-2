package site.nomoreparties.stellarburgers.tests.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientsResponseVO {
    Boolean success;

    // when success
    @JsonProperty("data")
    List<Ingredient> ingredientList;

    // when failure
    String message;
}
