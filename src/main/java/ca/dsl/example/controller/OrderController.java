package ca.dsl.example.controller;

import ca.dsl.example.domain.order.Order;
import ca.dsl.example.domain.order.OrderRequest;
import ca.dsl.example.domain.order.Payment;
import ca.dsl.example.domain.order.PaymentRequest;
import ca.dsl.example.domain.search.BasicPredicateBuilder;
import ca.dsl.example.service.OrderService;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/query")
  @ResponseBody
  public List<Order> query(@RequestParam String search) {
    BasicPredicateBuilder<Order> builder = new BasicPredicateBuilder<>(Order.class, "order");

    builder.from(search);
    BooleanExpression exp = builder.build();

    return this.orderService.search(exp);
  }

  @PostMapping
  @ResponseBody
  public Order create(@RequestBody OrderRequest request) {
    return this.orderService.save(Order.from(request));
  }

  @PostMapping("/{orderId}/payment")
  @ResponseBody
  public Order addPayment(@PathVariable String orderId, @RequestBody PaymentRequest request) {
    return this.orderService.addPayment(orderId, Payment.from(request));
  }

}
