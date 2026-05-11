package com.example.fatoura.core.application.port.inbound;

import java.util.UUID;

public interface ProcessInvoiceUseCase {
  void process(UUID invoiceId);
}
