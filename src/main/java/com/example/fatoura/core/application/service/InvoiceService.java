package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.DeleteInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.DownloadInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoicesUseCase;
import com.example.fatoura.core.application.port.inbound.ProcessInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.UploadInvoiceUseCase;
import com.example.fatoura.core.application.port.outbound.FileStoragePort;
import com.example.fatoura.core.application.port.outbound.InvoiceRepository;
import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.domain.event.InvoiceUploadedEvent;
import com.example.fatoura.core.domain.exception.ForbiddenException;
import com.example.fatoura.core.domain.exception.ResourceNotFoundException;
import com.example.fatoura.infrastructure.exception.InfrastructureException;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceStatus;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class InvoiceService implements UploadInvoiceUseCase, GetInvoicesUseCase, GetInvoiceUseCase, DownloadInvoiceUseCase, DeleteInvoiceUseCase, ProcessInvoiceUseCase {

  private final InvoiceRepository invoiceRepository;
  private final OrganizationRepository organizationRepository;
  private final MembershipRepository membershipRepository;
  private final FileStoragePort storagePort;
  private final DocumentTextExtractionService extractionService;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void process(UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

    processInvoice(invoice);
  }

  private void processInvoice(Invoice invoice) {
    invoice.setStatus(InvoiceStatus.PROCESSING);
    invoiceRepository.save(invoice);

    try {
      String rawText = extractionService.extract(invoice.getStoragePath(), invoice.getMimeType());
      invoice.setRawContent(rawText);
      invoice.setStatus(InvoiceStatus.PROCESSED);

    } catch (Exception e) {
      invoice.setStatus(InvoiceStatus.FAILED);
    }

    invoiceRepository.save(invoice);
  }

  @Override
  public void delete(User user, UUID invoiceId) {

      Invoice invoice = invoiceRepository.findById(invoiceId)
          .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

      boolean hasAccess = membershipRepository
          .existsByUserAndOrganization(user, invoice.getOrganization());

      if (!hasAccess) {
        throw new ForbiddenException("You do not have access to this invoice");
      }

      try {
        storagePort.delete(invoice.getStoragePath());
      } catch (IOException e) {
        throw new InfrastructureException("Could not delete file", e);
      }

      invoiceRepository.deleteById(invoiceId);
  }

  @Override
  public Resource download(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new ForbiddenException("You do not have access to this invoice");
    }

    return storagePort.loadAsResource(invoice.getStoragePath());
  }

  @Override
  public Invoice upload(
      User user,
      UUID organizationId,
      MultipartFile file
  ) throws IOException {

    Organization organization = organizationRepository
        .findById(organizationId)
        .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, organization);

    if (!hasAccess) {
      throw new ForbiddenException("You do not have access to this organization");
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

    Invoice savedInvoice = invoiceRepository.save(invoice);

    eventPublisher.publishEvent(new InvoiceUploadedEvent(savedInvoice.getId()));

    return savedInvoice;
  }

  @Override
  public List<Invoice> getByOrganization(User user, UUID organizationId) {
    Organization organization = organizationRepository
        .findById(organizationId)
        .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, organization);

    if (!hasAccess) {
      throw new ForbiddenException("You do not have access to this organization");
    }

    return invoiceRepository.findByOrganizationId(organizationId);
  }

  @Override
  public Invoice getById(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new ForbiddenException("You do not have access to this invoice");
    }

    return invoice;
  }
}
