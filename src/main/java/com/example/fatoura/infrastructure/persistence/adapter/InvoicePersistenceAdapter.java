package com.example.fatoura.infrastructure.persistence.adapter;

import com.example.fatoura.core.application.port.outbound.InvoiceRepository;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.infrastructure.persistence.entity.InvoiceEntity;
import com.example.fatoura.infrastructure.persistence.mapper.InvoicePersistenceMapper;
import com.example.fatoura.infrastructure.persistence.repository.JpaInvoiceRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoicePersistenceAdapter implements InvoiceRepository {

  private final JpaInvoiceRepository jpaInvoiceRepository;

  @Override
  public Invoice save(Invoice invoice) {
    InvoiceEntity entity = InvoicePersistenceMapper.toEntity(invoice);
    InvoiceEntity savedEntity = jpaInvoiceRepository.save(entity);
    return InvoicePersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public List<Invoice> findByOrganizationId(UUID organizationId) {
    return jpaInvoiceRepository.findByOrganizationId(organizationId).stream()
        .map(InvoicePersistenceMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Invoice> findById(UUID invoiceId) {
    return jpaInvoiceRepository.findById(invoiceId)
        .map(InvoicePersistenceMapper::toDomain);
  }

  @Override
  public void deleteById(UUID invoiceId) {
    jpaInvoiceRepository.deleteById(invoiceId);
  }
}
