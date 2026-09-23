package com.example.saga.inventory;
import jakarta.persistence.*;
@Entity
@Table(name="inventory_reservation", uniqueConstraints=@UniqueConstraint(columnNames={"orderId"}))
public class InventoryReservation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private Long orderId; private String productId; private int quantity; private String status;
 protected InventoryReservation(){}
 public InventoryReservation(Long orderId,String productId,int quantity){this.orderId=orderId;this.productId=productId;this.quantity=quantity;this.status="RESERVED";}
 public Long getId(){return id;} public Long getOrderId(){return orderId;} public String getProductId(){return productId;} public int getQuantity(){return quantity;} public String getStatus(){return status;}
 public void release(){status="RELEASED";}
}
