package com.example.saga.payment;
import com.example.saga.common.*; import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
@RestController @RequestMapping("/api/payments")
public class PaymentController {
 private final PaymentRepository repo; private final OutboxEventRepository outbox; private final ObjectMapper mapper;
 public PaymentController(PaymentRepository repo,OutboxEventRepository outbox,ObjectMapper mapper){this.repo=repo;this.outbox=outbox;this.mapper=mapper;}
 @PostMapping("/charge") @Transactional
 public PaymentEntity charge(@RequestBody PaymentRequest req) throws Exception {
   var existing=repo.findByOrderId(req.orderId()); if(existing.isPresent()) return existing.get();
   if(req.fail()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"simulated payment rejection");
   var p=repo.save(new PaymentEntity(req.orderId(),req.amount()));
   outbox.save(new OutboxEvent("PAYMENT",req.orderId().toString(),"PAYMENT_CHARGED",mapper.writeValueAsString(Map.of("type","PAYMENT_CHARGED","orderId",req.orderId())))); return p;
 }
 @PostMapping("/refund/{orderId}") @Transactional
 public PaymentEntity refund(@PathVariable Long orderId) throws Exception {
   var p=repo.findByOrderId(orderId).orElseThrow(); if(!"REFUNDED".equals(p.getStatus())) p.refund();
   outbox.save(new OutboxEvent("PAYMENT",orderId.toString(),"PAYMENT_REFUNDED",mapper.writeValueAsString(Map.of("type","PAYMENT_REFUNDED","orderId",orderId)))); return p;
 }
}
