package com.example.saga.worker;
import com.example.saga.common.OrderSagaInput; import io.temporal.activity.ActivityInterface;
@ActivityInterface
public interface SagaActivities {
 void updateOrder(Long orderId,String status,String reason);
 void reserveInventory(OrderSagaInput input);
 void releaseInventory(Long orderId);
 void chargePayment(OrderSagaInput input);
 void refundPayment(Long orderId);
 void createShipment(OrderSagaInput input);
 void cancelShipment(Long orderId);
}
