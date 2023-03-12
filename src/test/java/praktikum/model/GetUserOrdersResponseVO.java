package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserOrdersResponseVO {
    Boolean success;

    // when success
    Long total;
    Long totalToday;
    List<OrderGetResultVO> orders;

    // when failure
    String message;
}
