package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.UploadInvoiceUseCase;
import com.example.fatoura.core.application.port.outbound.FileStoragePort;
import com.example.fatoura.core.application.port.outbound.InvoiceRepository;
import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceStatus;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class InvoiceService implements UploadInvoiceUseCase {

  private final InvoiceRepository invoiceRepository;
  private final OrganizationRepository organizationRepository;
  private final MembershipRepository membershipRepository;
  private final FileStoragePort storagePort;

  @Override
  public Invoice upload(
      User user,
      UUID organizationId,
      MultipartFile file
  ) throws IOException {

    Organization organization = organizationRepository
        .findById(organizationId)
        .orElseThrow(() -> new RuntimeException("Organization not found"));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, organization);

    if (!hasAccess) {
      throw new RuntimeException("Forbidden");
    }

    String path = storagePort.store(
        file.getBytes(),
        file.getOriginalFilename(),
        file.getContentType()
    );

    Invoice invoice = Invoice.builder()
        .filename(file.getOriginalFilename())
        .storagePath(path)
        .mimeType(file.getContentType())
        .fileSize(file.getSize())
        .uploadedAt(Instant.now())
        .status(InvoiceStatus.UPLOADED)
        .organization(organization)
        .uploadedBy(user)
        .build();

    return invoiceRepository.save(invoice);
  }
}
