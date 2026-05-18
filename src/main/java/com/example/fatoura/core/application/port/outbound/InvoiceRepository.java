package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceSearchCriteria;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceRepository {
  Invoice save(Invoice invoice);
  List<Invoice> findByOrganizationId(UUID organizationId);
  Optional<Invoice> findById(UUID invoiceId);
  void deleteById(UUID invoiceId);

  Page<Invoice> search(
      InvoiceSearchCriteria criteria,
      Pageable pageable
  );
}
