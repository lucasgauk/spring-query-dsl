package ca.dsl.example.service;

import ca.dsl.example.domain.order.Order;
import ca.dsl.example.domain.order.Payment;
import com.querydsl.core.types.Predicate;
import java.util.List;

public interface OrderService {

    List<Order> search(Predicate p);

    Order save(Order order);

    void saveAll(List<Order> orders);

    Order addPayment(String orderId, Payment payment);

}
