package ca.dsl.example.service;

import ca.dsl.example.domain.order.Order;
import ca.dsl.example.domain.order.Payment;
import com.querydsl.core.types.Predicate;

public interface OrderService {

    Iterable<Order> search(Predicate p);

    Order save(Order order);

    Order addPayment(String orderId, Payment payment);

}
