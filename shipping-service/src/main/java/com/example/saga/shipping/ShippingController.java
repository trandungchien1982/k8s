package com.example.saga.shipping;
import com.example.saga.common.*; import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
@RestController @RequestMapping("/api/shipping")
public class ShippingController {
 private final ShippingRepository repo; private final OutboxEventRepository outbox; private final ObjectMapper mapper;
 public ShippingController(ShippingRepository repo,OutboxEventRepository outbox,ObjectMapper mapper){this.repo=repo;this.outbox=outbox;this.mapper=mapper;}
 @PostMapping("/create") @Transactional
 public Shipment create(@RequestBody ShippingRequest req) throws Exception {
   var existing=repo.findByOrderId(req.orderId()); if(existing.isPresent()) return existing.get();
   if(req.fail()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"simulated shipping rejection");
   var s=repo.save(new Shipment(req.orderId(),req.productId(),req.quantity()));
   outbox.save(new OutboxEvent("SHIPPING",req.orderId().toString(),"SHIPMENT_CREATED",mapper.writeValueAsString(Map.of("type","SHIPMENT_CREATED","orderId",req.orderId())))); return s;
 }
 @PostMapping("/cancel/{orderId}") @Transactional
 public Shipment cancel(@PathVariable Long orderId) throws Exception {
   var s=repo.findByOrderId(orderId).orElseThrow(); if(!"CANCELLED".equals(s.getStatus())) s.cancel();
   outbox.save(new OutboxEvent("SHIPPING",orderId.toString(),"SHIPMENT_CANCELLED",mapper.writeValueAsString(Map.of("type","SHIPMENT_CANCELLED","orderId",orderId)))); return s;
 }
}
