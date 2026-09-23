package com.example.saga.shipping;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional;
public interface ShippingRepository extends JpaRepository<Shipment,Long>{Optional<Shipment> findByOrderId(Long orderId);}
