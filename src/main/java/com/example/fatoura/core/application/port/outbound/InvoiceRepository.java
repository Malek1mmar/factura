package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.Invoice;
import java.util.List;
import java.util.UUID;

public interface InvoiceRepository {
  Invoice save(Invoice invoice);
  List<Invoice> findByOrganizationId(UUID organizationId);
}
