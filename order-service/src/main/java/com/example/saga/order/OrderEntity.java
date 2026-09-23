package com.example.saga.order;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="orders")
public class OrderEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private int quantity;
    private BigDecimal amount;
    private String status;
    @Column(columnDefinition="text")
    private String failureReason;
    protected OrderEntity() {}
    public OrderEntity(String productId, int quantity, BigDecimal amount) {
        this.productId=productId; this.quantity=quantity; this.amount=amount; this.status="PENDING";
    }
    public Long getId(){return id;} public String getProductId(){return productId;} public int getQuantity(){return quantity;}
    public BigDecimal getAmount(){return amount;} public String getStatus(){return status;} public String getFailureReason(){return failureReason;}
    public void updateStatus(String status, String reason){this.status=status; this.failureReason=reason;}
}
