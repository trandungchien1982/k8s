package com.example.saga.inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface InventoryRepository extends JpaRepository<InventoryReservation,Long>{ Optional<InventoryReservation> findByOrderId(Long orderId); }
