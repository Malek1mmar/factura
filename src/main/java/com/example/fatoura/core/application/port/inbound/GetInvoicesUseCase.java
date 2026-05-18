package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceSearchCriteria;
import com.example.fatoura.core.domain.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetInvoicesUseCase {
  List<Invoice> getByOrganization(User user, UUID organizationId);

  Page<Invoice> search(
      User user,
      InvoiceSearchCriteria criteria,
      Pageable pageable
  );
}
