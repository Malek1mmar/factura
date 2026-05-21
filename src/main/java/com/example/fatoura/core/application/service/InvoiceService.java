package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.DeleteInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.DownloadInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoicesUseCase;
import com.example.fatoura.core.application.port.inbound.ProcessInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.ReviewInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.UpdateInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.UploadInvoiceUseCase;
import com.example.fatoura.core.application.port.outbound.FileStoragePort;
import com.example.fatoura.core.application.port.outbound.InvoiceRepository;
import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.domain.constant.MessageConstants;
import com.example.fatoura.core.domain.event.InvoiceUploadedEvent;
import com.example.fatoura.core.domain.exception.ForbiddenException;
import com.example.fatoura.core.domain.exception.ResourceNotFoundException;
import com.example.fatoura.infrastructure.exception.InfrastructureException;
import com.example.fatoura.core.application.port.outbound.InvoiceParsingPort;
import com.example.fatoura.core.domain.model.ExtractedInvoiceData;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceSearchCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class InvoiceService implements UploadInvoiceUseCase, GetInvoicesUseCase, GetInvoiceUseCase, DownloadInvoiceUseCase, DeleteInvoiceUseCase, ProcessInvoiceUseCase, ReviewInvoiceUseCase, UpdateInvoiceUseCase {

  private final InvoiceRepository invoiceRepository;
  private final OrganizationRepository organizationRepository;
  private final MembershipRepository membershipRepository;
  private final FileStoragePort storagePort;
  private final DocumentTextExtractionService extractionService;
  private final InvoiceParsingPort parsingPort;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void approve(User user, UUID invoiceId) {
    Invoice invoice = getInvoiceWithAccessCheck(user, invoiceId);
    invoice.setStatus(InvoiceStatus.APPROVED);
    invoiceRepository.save(invoice);
  }

  @Override
  public void reject(User user, UUID invoiceId) {
    Invoice invoice = getInvoiceWithAccessCheck(user, invoiceId);
    invoice.setStatus(InvoiceStatus.REJECTED);
    invoiceRepository.save(invoice);
  }

  @Override
  public Invoice update(User user, UUID invoiceId, UpdateCommand command) {
    Invoice invoice = getInvoiceWithAccessCheck(user, invoiceId);

    if (command.getSupplierName() != null) invoice.setSupplierName(command.getSupplierName());
    if (command.getInvoiceNumber() != null) invoice.setInvoiceNumber(command.getInvoiceNumber());
    if (command.getTotalAmount() != null) invoice.setTotalAmount(command.getTotalAmount());
    if (command.getInvoiceDate() != null) invoice.setInvoiceDate(command.getInvoiceDate());
    if (command.getCurrency() != null) invoice.setCurrency(command.getCurrency());

    if (invoice.getStatus() == InvoiceStatus.REVIEW_REQUIRED) {
        invoice.setStatus(InvoiceStatus.PROCESSED);
    }

    return invoiceRepository.save(invoice);
  }

  private Invoice getInvoiceWithAccessCheck(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.INVOICE_NOT_FOUND, invoiceId)));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new ForbiddenException(MessageConstants.ACCESS_DENIED_INVOICE);
    }

    return invoice;
  }

  @Override
  public Page<Invoice> search(User user, InvoiceSearchCriteria criteria, Pageable pageable) {
    if (criteria.getOrganizationId() == null) {
      throw new IllegalArgumentException("Organization ID is required");
    }

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganizationId(user, criteria.getOrganizationId());

    if (!hasAccess) {
      throw new ForbiddenException(MessageConstants.ACCESS_DENIED_ORGANIZATION);
    }

    return invoiceRepository.search(criteria, pageable);
  }

  @Override
  public void process(UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.INVOICE_NOT_FOUND, invoiceId)));

    processInvoice(invoice);
  }

  private void processInvoice(Invoice invoice) {
    invoice.setStatus(InvoiceStatus.PROCESSING);
    invoiceRepository.save(invoice);

    try {
      String rawText = extractionService.extract(invoice.getStoragePath(), invoice.getMimeType());
      invoice.setRawContent(rawText);

      ExtractedInvoiceData data = parsingPort.parse(rawText);
      invoice.setSupplierName(data.getSupplierName());
      invoice.setInvoiceNumber(data.getInvoiceNumber());
      invoice.setTotalAmount(data.getTotalAmount());
      invoice.setInvoiceDate(data.getInvoiceDate());
      invoice.setCurrency(data.getCurrency());

      if (isReviewRequired(invoice)) {
        invoice.setStatus(InvoiceStatus.REVIEW_REQUIRED);
      } else {
        invoice.setStatus(InvoiceStatus.PROCESSED);
      }

    } catch (Exception e) {
      invoice.setStatus(InvoiceStatus.FAILED);
    }

    invoiceRepository.save(invoice);
  }

  private boolean isReviewRequired(Invoice invoice) {
    return invoice.getSupplierName() == null ||
        invoice.getTotalAmount() == null ||
        invoice.getRawContent() == null ||
        invoice.getRawContent().isBlank();
  }

  @Override
  public void delete(User user, UUID invoiceId) {

      Invoice invoice = invoiceRepository.findById(invoiceId)
          .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.INVOICE_NOT_FOUND, invoiceId)));

      boolean hasAccess = membershipRepository
          .existsByUserAndOrganization(user, invoice.getOrganization());

      if (!hasAccess) {
        throw new ForbiddenException(MessageConstants.ACCESS_DENIED_INVOICE);
      }

      try {
        storagePort.delete(invoice.getStoragePath());
      } catch (IOException e) {
        throw new InfrastructureException(MessageConstants.FILE_DELETE_ERROR, e);
      }

      invoiceRepository.deleteById(invoiceId);
  }

  @Override
  public Resource download(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.INVOICE_NOT_FOUND, invoiceId)));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new ForbiddenException(MessageConstants.ACCESS_DENIED_INVOICE);
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
        .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.ORGANIZATION_NOT_FOUND, organizationId)));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, organization);

    if (!hasAccess) {
      throw new ForbiddenException(MessageConstants.ACCESS_DENIED_ORGANIZATION);
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
        .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.ORGANIZATION_NOT_FOUND, organizationId)));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, organization);

    if (!hasAccess) {
      throw new ForbiddenException(MessageConstants.ACCESS_DENIED_ORGANIZATION);
    }

    return invoiceRepository.findByOrganizationId(organizationId);
  }

  @Override
  public Invoice getById(User user, UUID invoiceId) {
    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.INVOICE_NOT_FOUND, invoiceId)));

    boolean hasAccess = membershipRepository
        .existsByUserAndOrganization(user, invoice.getOrganization());

    if (!hasAccess) {
      throw new ForbiddenException(MessageConstants.ACCESS_DENIED_INVOICE);
    }

    return invoice;
  }
}
