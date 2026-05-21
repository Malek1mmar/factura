package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.User;
import java.util.UUID;

public interface ReviewInvoiceUseCase {
  void approve(User user, UUID invoiceId);
  void reject(User user, UUID invoiceId);
}
