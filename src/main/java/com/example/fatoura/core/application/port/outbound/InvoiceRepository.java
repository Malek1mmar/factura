package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.Invoice;

public interface InvoiceRepository {
  Invoice save(Invoice invoice);
}
