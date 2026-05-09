package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.User;
import java.util.List;
import java.util.UUID;

public interface GetInvoicesUseCase {
  List<Invoice> getByOrganization(User user, UUID organizationId);
}
