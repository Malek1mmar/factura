package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

public interface UpdateInvoiceUseCase {

  Invoice update(User user, UUID invoiceId, UpdateCommand command);

  @Value
  @Builder
  class UpdateCommand {
    String supplierName;
    String invoiceNumber;
    BigDecimal totalAmount;
    LocalDate invoiceDate;
    String currency;
  }
}
