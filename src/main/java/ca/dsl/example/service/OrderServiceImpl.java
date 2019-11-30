package ca.dsl.example.service;

import ca.dsl.example.domain.order.Order;
import ca.dsl.example.domain.order.Order.OrderStatus;
import ca.dsl.example.domain.order.OrderNotFoundException;
import ca.dsl.example.domain.order.OrderRepository;
import ca.dsl.example.domain.order.Payment;
import com.querydsl.core.types.Predicate;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    public Iterable<Order> search(Predicate p) {
        return this.repository.findAll(p);
    }

    public Order save(Order order) {
        return this.repository.save(order);
    }

    public Order addPayment(String orderId, Payment payment) {
        Order order = this.repository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.getPayments().add(payment);
        BigDecimal paymentTotal = order.getPayments()
                                       .stream()
                                       .map(Payment::getAmount)
                                       .reduce(BigDecimal::add)
                                       .orElse(BigDecimal.ZERO);
        if (paymentTotal.compareTo(order.getOrderTotal()) >= 0) {
            order.setOrderStatus(OrderStatus.COMPLETED.toString());
        }
        return this.save(order);
    }
}
