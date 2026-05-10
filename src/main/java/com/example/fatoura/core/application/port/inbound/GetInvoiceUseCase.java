package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.User;
import java.util.UUID;

public interface GetInvoiceUseCase {

  Invoice getById(
      User user,
      UUID invoiceId
  );
}
