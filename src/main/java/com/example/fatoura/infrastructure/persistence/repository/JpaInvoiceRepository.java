package com.example.fatoura.infrastructure.persistence.repository;

import com.example.fatoura.infrastructure.persistence.entity.InvoiceEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {
  List<InvoiceEntity> findByOrganizationId(UUID organizationId);
}