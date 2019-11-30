package ca.dsl.example.domain.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(String orderId) {
    super("Order id: " + orderId + " not found");
  }
}
