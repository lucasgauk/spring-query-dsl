package ca.dsl.example.domain.order;

import ca.dsl.example.domain.internal.BaseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@Setter
@TypeAlias("order")
@AllArgsConstructor
public class Order extends BaseEntity {

  private String customerName;
  private BigDecimal orderTotal;
  private String orderStatus;
  private List<Payment> payments;

  public enum OrderStatus {
    IN_PROGRESS, COMPLETED
  }

  public static Order from(OrderRequest request) {
    return new Order(request.getCustomerName(),
                     request.getOrderTotal(),
                     OrderStatus.IN_PROGRESS.toString(),
                     new ArrayList<>());
  }

}


