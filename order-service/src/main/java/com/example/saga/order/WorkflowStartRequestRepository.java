package com.example.saga.order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkflowStartRequestRepository extends JpaRepository<WorkflowStartRequestEntity, Long> {
    List<WorkflowStartRequestEntity> findTop20ByStartedFalseOrderByIdAsc();
}
