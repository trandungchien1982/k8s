package com.example.saga.payment;
import jakarta.persistence.*; import java.math.BigDecimal;
@Entity @Table(name="payment",uniqueConstraints=@UniqueConstraint(columnNames={"orderId"}))
public class PaymentEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false) private Long orderId; private BigDecimal amount; private String status;
 protected PaymentEntity(){} public PaymentEntity(Long orderId,BigDecimal amount){this.orderId=orderId;this.amount=amount;this.status="CHARGED";}
 public Long getId(){return id;} public Long getOrderId(){return orderId;} public BigDecimal getAmount(){return amount;} public String getStatus(){return status;} public void refund(){status="REFUNDED";}
}
