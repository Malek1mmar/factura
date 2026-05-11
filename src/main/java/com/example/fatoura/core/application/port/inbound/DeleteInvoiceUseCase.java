package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.User;
import java.util.UUID;

public interface DeleteInvoiceUseCase {
  void delete(User user, UUID invoiceId);
}
