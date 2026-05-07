package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.User;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UploadInvoiceUseCase {
  Invoice upload(User user, UUID organizationId, MultipartFile file) throws IOException;
}
