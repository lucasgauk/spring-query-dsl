package ca.dsl.example.domain.order;

import ca.dsl.example.domain.internal.BaseEntity;
import com.querydsl.core.annotations.QueryEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@QueryEntity
@Document
@TypeAlias("order")
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

  private String customerName;
  private BigDecimal orderTotal;
  private String orderStatus;
  private List<Payment> payments = new ArrayList<>();
  private LocalDateTime orderClosedAt;

  public enum OrderStatus {
    IN_PROGRESS, COMPLETED
  }

  public static Order from(OrderRequest request) {
    return new Order(request.getCustomerName(),
                     request.getOrderTotal(),
                     OrderStatus.IN_PROGRESS.toString(),
                     new ArrayList<>(),
                     null);
  }

}


