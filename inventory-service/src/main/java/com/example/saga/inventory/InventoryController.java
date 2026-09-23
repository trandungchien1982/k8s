package com.example.saga.inventory;
import com.example.saga.common.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/inventory")
public class InventoryController {
 private final InventoryRepository repo; private final OutboxEventRepository outbox; private final ObjectMapper mapper;
 public InventoryController(InventoryRepository repo,OutboxEventRepository outbox,ObjectMapper mapper){this.repo=repo;this.outbox=outbox;this.mapper=mapper;}
 @PostMapping("/reserve") @Transactional
 public InventoryReservation reserve(@RequestBody ReserveInventoryRequest req) throws Exception {
   var existing=repo.findByOrderId(req.orderId()); if(existing.isPresent()) return existing.get();
   if(req.quantity()<=0 || req.quantity()>100) throw new IllegalArgumentException("quantity must be 1..100");
   var r=repo.save(new InventoryReservation(req.orderId(),req.productId(),req.quantity()));
   outbox.save(new OutboxEvent("INVENTORY",req.orderId().toString(),"INVENTORY_RESERVED",mapper.writeValueAsString(Map.of("type","INVENTORY_RESERVED","orderId",req.orderId()))));
   return r;
 }
 @PostMapping("/release/{orderId}") @Transactional
 public InventoryReservation release(@PathVariable Long orderId) throws Exception {
   var r=repo.findByOrderId(orderId).orElseThrow(); if(!"RELEASED".equals(r.getStatus())) r.release();
   outbox.save(new OutboxEvent("INVENTORY",orderId.toString(),"INVENTORY_RELEASED",mapper.writeValueAsString(Map.of("type","INVENTORY_RELEASED","orderId",orderId)))); return r;
 }
}
