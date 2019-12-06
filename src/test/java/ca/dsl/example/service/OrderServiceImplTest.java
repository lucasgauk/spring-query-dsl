package ca.dsl.example.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ca.dsl.example.domain.order.Order;
import ca.dsl.example.domain.order.Order.OrderStatus;
import ca.dsl.example.domain.order.Payment;
import ca.dsl.example.domain.order.PaymentRequest;
import ca.dsl.example.domain.search.BasicPredicateBuilder;
import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceImplTest {

    @Autowired private OrderService orderService;
    @Autowired private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() {
        mongoTemplate.getCollectionNames().forEach(s -> mongoTemplate.getCollection(s).drop());
    }

    @Test
    public void invalidSearchForOrder() {
        Order o1 = new Order();
        Order o2 = new Order();

        this.orderService.saveAll(Arrays.asList(o1, o2));

        String query1 = "fasggq:33"; // Valid format, nonexistent property, should return empty list
        String query2 = "6&3s>,ga3"; // Invalid format, should throw exception

        BasicPredicateBuilder<Order> builder1 = new BasicPredicateBuilder<>(Order.class, "order");
        builder1.from(query1);
        List<Order> query1Orders = Lists.newArrayList(this.orderService.search(builder1.build()));

        assertThat(query1Orders.size()).isEqualTo(0);

        BasicPredicateBuilder<Order> builder2 = new BasicPredicateBuilder<>(Order.class, "order");

        Assertions.assertThrows(IllegalArgumentException.class, () -> builder2.from(query2)); // Should throw exception
    }

    @Test
    public void searchForOrderByStatusAndCreatedDate() {
        Order o1 = new Order();
        o1.setOrderStatus(OrderStatus.COMPLETED.toString());
        Order o2 = new Order();
        o2.setOrderStatus(OrderStatus.IN_PROGRESS.toString());
        Order o3 = new Order();
        o3.setOrderStatus(OrderStatus.COMPLETED.toString());

        this.orderService.saveAll(Arrays.asList(o1, o2, o3));

        o1.setCreatedAt(o1.getCreatedAt().minusDays(10));
        o2.setCreatedAt(o2.getCreatedAt().minusDays(5));

        this.orderService.saveAll(Arrays.asList(o1, o2));

        String query1 = "orderStatus:IN_PROGRESS,createdAt>" + LocalDate.now().minusDays(6).toString();
        String query2 = "orderStatus:COMPLETED,createdAt>" + LocalDate.now().minusDays(6).toString();
        String query3 = "orderStatus:COMPLETED,createdAt<" + LocalDate.now().minusDays(1).toString();

        BasicPredicateBuilder<Order> builder1 = new BasicPredicateBuilder<>(Order.class, "order");
        builder1.from(query1);
        List<Order> query1Orders = this.orderService.search(builder1.build());

        assertThat(query1Orders.size()).isEqualTo(1);
        assertThat(query1Orders.get(0).getId()).isEqualTo(o2.getId());

        BasicPredicateBuilder<Order> builder2 = new BasicPredicateBuilder<>(Order.class, "order");
        builder2.from(query2);
        List<Order> query2Orders = this.orderService.search(builder2.build());

        assertThat(query2Orders.size()).isEqualTo(1);
        assertThat(query2Orders.get(0).getId()).isEqualTo(o3.getId());

        BasicPredicateBuilder<Order> builder3 = new BasicPredicateBuilder<>(Order.class, "order");
        builder3.from(query3);
        List<Order> query3Orders = this.orderService.search(builder3.build());

        assertThat(query3Orders.size()).isEqualTo(1);
        assertThat(query3Orders.get(0).getId()).isEqualTo(o1.getId());
    }

    @Test
    public void searchForOrderByPayment() {
        Order o1 = new Order();
        o1.setOrderTotal(BigDecimal.ZERO);
        Order o2 = new Order();
        o2.setOrderTotal(BigDecimal.ONE);
        Order o3 = new Order();
        o3.setOrderTotal(BigDecimal.TEN);

        PaymentRequest p1 = new PaymentRequest(BigDecimal.ZERO);
        PaymentRequest p2 = new PaymentRequest(BigDecimal.ONE);
        PaymentRequest p3 = new PaymentRequest(BigDecimal.TEN);

        this.orderService.saveAll(Arrays.asList(o1, o2, o3));

        this.orderService.addPayment(o1.getId().toString(), Payment.from(p1));
        this.orderService.addPayment(o2.getId().toString(), Payment.from(p2));
        this.orderService.addPayment(o3.getId().toString(), Payment.from(p3));


        String query1 = "payments.amount>1";
        String query2 = "payments.amount:0";
        String query3 = "payments.amount<5";

        BasicPredicateBuilder<Order> builder1 = new BasicPredicateBuilder<>(Order.class, "order");
        builder1.from(query1);
        List<Order> query1Orders = this.orderService.search(builder1.build());

        assertThat(query1Orders.size()).isEqualTo(1);
        assertThat(query1Orders.get(0).getId()).isEqualTo(o3.getId());

        BasicPredicateBuilder<Order> builder2 = new BasicPredicateBuilder<>(Order.class, "order");
        builder2.from(query2);
        List<Order> query2Orders = this.orderService.search(builder2.build());

        assertThat(query2Orders.size()).isEqualTo(1);
        assertThat(query2Orders.get(0).getId()).isEqualTo(o1.getId());

        BasicPredicateBuilder<Order> builder3 = new BasicPredicateBuilder<>(Order.class, "order");
        builder3.from(query3);
        List<Order> query3Orders = this.orderService.search(builder3.build());

        assertThat(query3Orders.size()).isEqualTo(2);
    }
}