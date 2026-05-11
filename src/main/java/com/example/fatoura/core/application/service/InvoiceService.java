package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.DeleteInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.DownloadInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoicesUseCase;
import com.example.fatoura.core.application.port.inbound.ProcessInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.UploadInvoiceUseCase;
import com.example.fatoura.core.application.port.outbound.FileStoragePort;
import com.example.fatoura.core.application.port.outbound.InvoiceOcrPort;
import com.example.fatoura.core.application.port.outbound.InvoiceRepository;
import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.domain.model.ExtractedInvoiceData;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceStatus;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class InvoiceService implements UploadInvoiceUseCase, GetInvoicesUseCase, GetInvoiceUseCase, DownloadInvoiceUseCase, DeleteInvoiceUseCase, ProcessInvoiceUseCase {

  private final InvoiceRepository invoiceRepository;
  private final OrganizationRepository organizationRepository;
  private final MembershipRepository membershipRepository;
  private final FileStoragePort storagePort;
  private final InvoiceOcrPort ocrPort;

  @Override
  public void process(UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new RuntimeException("Invoice not found"));

    processInvoice(invoice);
  }

  private Invoice processInvoice(Invoice invoice) {
    invoice.setStatus(InvoiceStatus.PROCESSING);
    invoiceRepository.save(invoice);

    try {
      ExtractedInvoiceData data = ocrPort.process(invoice.getStoragePath());

      invoice.setSupplierName(data.getSupplierName());
      invoice.setInvoiceNumber(data.getInvoiceNumber());
      invoice.setTotalAmount(data.getTotalAmount());
      invoice.setInvoiceDate(data.getInvoiceDate());
      invoice.setCurrency(data.getCurrency());
      invoice.setStatus(InvoiceStatus.PROCESSED);

    } catch (Exception e) {
      invoice.setStatus(InvoiceStatus.FAILED);
    }

    return invoiceRepository.save(invoice);
  }

  @Override
  public void delete(User user, UUID invoiceId) {

      Invoice invoice = invoiceRepository.findById(invoiceId)
          .orElseThrow(() -> new RuntimeException("Invoice not found"));

      boolean hasAccess = membershipRepository
          .existsByUserAndOrganization(user, invoice.getOrganization());

      if (!hasAccess) {
        throw new RuntimeException("Forbidden");
      }

      try {
        storagePort.delete(invoice.getStoragePath());
      } catch (IOException e) {
        throw new RuntimeException("Could not delete file", e);
      }

      invoiceRepository.deleteById(invoiceId);
  }

  @Override
  public Resource download(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new RuntimeException("Invoice not found"));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new RuntimeException("Forbidden");
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

    Invoice savedInvoice = invoiceRepository.save(invoice);
    return processInvoice(savedInvoice);
  }

  @Override
  public List<Invoice> getByOrganization(User user, UUID organizationId) {
    Organization organization = organizationRepository
        .findById(organizationId)
        .orElseThrow(() -> new RuntimeException("Organization not found"));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, organization);

    if (!hasAccess) {
      throw new RuntimeException("Forbidden");
    }

    return invoiceRepository.findByOrganizationId(organizationId);
  }

  @Override
  public Invoice getById(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new RuntimeException("Invoice not found"));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new RuntimeException("Forbidden");
    }

    return invoice;
  }
}
