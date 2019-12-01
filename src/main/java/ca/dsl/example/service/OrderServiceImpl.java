package ca.dsl.example.service;

import ca.dsl.example.domain.order.Order;
import ca.dsl.example.domain.order.Order.OrderStatus;
import ca.dsl.example.domain.order.OrderNotFoundException;
import ca.dsl.example.domain.order.OrderRepository;
import ca.dsl.example.domain.order.Payment;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> search(Predicate p) {
        return Lists.newArrayList(this.repository.findAll(p));
    }

    public Order save(Order order) {
        return this.repository.save(order);
    }

    public void saveAll(List<Order> orders) {
        this.repository.saveAll(orders);
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
            this.closeOrder(order);
        }
        return this.save(order);
    }

    private void closeOrder(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETED.toString());
        order.setOrderClosedAt(LocalDateTime.now());
    }
}
