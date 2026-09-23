package com.example.saga.shipping;
import jakarta.persistence.*;
@Entity @Table(name="shipment",uniqueConstraints=@UniqueConstraint(columnNames={"orderId"}))
public class Shipment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false) private Long orderId; private String productId; private int quantity; private String status;
 protected Shipment(){} public Shipment(Long orderId,String productId,int quantity){this.orderId=orderId;this.productId=productId;this.quantity=quantity;this.status="CREATED";}
 public Long getId(){return id;} public Long getOrderId(){return orderId;} public String getProductId(){return productId;} public int getQuantity(){return quantity;} public String getStatus(){return status;} public void cancel(){status="CANCELLED";}
}
