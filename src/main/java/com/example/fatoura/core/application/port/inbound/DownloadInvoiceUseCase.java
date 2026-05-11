package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.User;
import java.util.UUID;
import org.springframework.core.io.Resource;

public interface DownloadInvoiceUseCase {
  Resource download(User user, UUID invoiceId);
}
