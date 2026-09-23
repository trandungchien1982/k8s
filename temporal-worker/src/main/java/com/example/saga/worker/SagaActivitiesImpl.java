package com.example.saga.worker;
import com.example.saga.common.*;
import io.temporal.failure.ApplicationFailure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class SagaActivitiesImpl implements SagaActivities {
 private final RestClient client;
 private final String orderUrl, inventoryUrl, paymentUrl, shippingUrl;
 public SagaActivitiesImpl(RestClient.Builder builder,
   @Value("${services.order:http://order-service:8080}") String orderUrl,
   @Value("${services.inventory:http://inventory-service:8080}") String inventoryUrl,
   @Value("${services.payment:http://payment-service:8080}") String paymentUrl,
   @Value("${services.shipping:http://shipping-service:8080}") String shippingUrl) {
   this.client=builder.build(); this.orderUrl=orderUrl; this.inventoryUrl=inventoryUrl; this.paymentUrl=paymentUrl; this.shippingUrl=shippingUrl;
 }
 private void post(String uri,Object body){
   try { client.post().uri(uri).body(body).retrieve().toBodilessEntity(); }
   catch(HttpClientErrorException e){ if(e.getStatusCode().value()==422 || e.getStatusCode().is4xxClientError()) throw ApplicationFailure.newNonRetryableFailure(e.getResponseBodyAsString(),"BUSINESS_ERROR"); throw e; }
 }
 public void updateOrder(Long id,String status,String reason){ client.patch().uri(orderUrl+"/api/orders/"+id+"/status").body(new StatusUpdateRequest(status,reason)).retrieve().toBodilessEntity(); }
 public void reserveInventory(OrderSagaInput i){post(inventoryUrl+"/api/inventory/reserve",new ReserveInventoryRequest(i.orderId(),i.productId(),i.quantity()));}
 public void releaseInventory(Long id){post(inventoryUrl+"/api/inventory/release/"+id,"");}
 public void chargePayment(OrderSagaInput i){post(paymentUrl+"/api/payments/charge",new PaymentRequest(i.orderId(),i.amount(),i.failPayment()));}
 public void refundPayment(Long id){post(paymentUrl+"/api/payments/refund/"+id,"");}
 public void createShipment(OrderSagaInput i){post(shippingUrl+"/api/shipping/create",new ShippingRequest(i.orderId(),i.productId(),i.quantity(),i.failShipping()));}
 public void cancelShipment(Long id){post(shippingUrl+"/api/shipping/cancel/"+id,"");}
}
