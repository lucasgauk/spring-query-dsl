package ca.dsl.example.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payment {

    LocalDateTime processedAt;
    BigDecimal amount;

    public static Payment from(PaymentRequest request) {
        return new Payment(LocalDateTime.now(), request.getAmount());
    }

}
