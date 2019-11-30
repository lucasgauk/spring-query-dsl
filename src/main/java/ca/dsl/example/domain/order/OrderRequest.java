package ca.dsl.example.domain.order;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderRequest {

    private String customerName;
    private BigDecimal orderTotal;

}
