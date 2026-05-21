package com.example.fatoura.infrastructure.web.controller;

import com.example.fatoura.core.application.port.inbound.DeleteInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.DownloadInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.GetInvoicesUseCase;
import com.example.fatoura.core.application.port.inbound.ReviewInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.UpdateInvoiceUseCase;
import com.example.fatoura.core.application.port.inbound.UploadInvoiceUseCase;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.InvoiceSearchCriteria;
import com.example.fatoura.core.domain.model.User;
import com.example.fatoura.infrastructure.web.dto.InvoiceResponse;
import com.example.fatoura.infrastructure.web.dto.UpdateInvoiceRequest;
import com.example.fatoura.infrastructure.web.mapper.InvoiceWebMapper;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

  private final UploadInvoiceUseCase uploadInvoiceUseCase;
  private final GetInvoicesUseCase getInvoicesUseCase;
  private final GetInvoiceUseCase getInvoiceUseCase;
  private final DownloadInvoiceUseCase downloadInvoiceUseCase;
  private final DeleteInvoiceUseCase deleteInvoiceUseCase;
  private final ReviewInvoiceUseCase reviewInvoiceUseCase;
  private final UpdateInvoiceUseCase updateInvoiceUseCase;

  @PostMapping(
      value = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public InvoiceResponse upload(
      User user,
      @RequestParam UUID organizationId,
      @RequestParam("file") MultipartFile file
  ) throws IOException {

    Invoice invoice = uploadInvoiceUseCase.upload(
        user,
        organizationId,
        file
    );
    return InvoiceWebMapper.toResponse(invoice);
  }

  @PostMapping("/{id}/approve")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void approve(
      User user,
      @PathVariable UUID id
  ) {
    reviewInvoiceUseCase.approve(user, id);
  }

  @PostMapping("/{id}/reject")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void reject(
      User user,
      @PathVariable UUID id
  ) {
    reviewInvoiceUseCase.reject(user, id);
  }

  @PatchMapping("/{id}")
  public InvoiceResponse update(
      User user,
      @PathVariable UUID id,
      @RequestBody UpdateInvoiceRequest request
  ) {
    UpdateInvoiceUseCase.UpdateCommand command = UpdateInvoiceUseCase.UpdateCommand.builder()
        .supplierName(request.getSupplierName())
        .invoiceNumber(request.getInvoiceNumber())
        .totalAmount(request.getTotalAmount())
        .invoiceDate(request.getInvoiceDate())
        .currency(request.getCurrency())
        .build();

    Invoice updatedInvoice = updateInvoiceUseCase.update(user, id, command);
    return InvoiceWebMapper.toResponse(updatedInvoice);
  }

  @GetMapping
  public Page<InvoiceResponse> search(
      User user,
      InvoiceSearchCriteria criteria,
      Pageable pageable
  ) {
    return getInvoicesUseCase.search(user, criteria, pageable)
        .map(InvoiceWebMapper::toResponse);
  }

  @GetMapping("/old")
  public List<InvoiceResponse> getAll(
      User user,
      @RequestParam UUID organizationId
  ) {

    return getInvoicesUseCase.getByOrganization(user, organizationId)
        .stream()
        .map(InvoiceWebMapper::toResponse)
        .toList();
  }

  @GetMapping("/{id}")
  public InvoiceResponse getById(
      User user,
      @PathVariable UUID id
  ) {
    Invoice invoice = getInvoiceUseCase.getById(user, id);

    return InvoiceWebMapper.toResponse(invoice);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(
      User user,
      @PathVariable UUID id
  ) {
    Invoice invoice = getInvoiceUseCase.getById(user, id);
    Resource resource = downloadInvoiceUseCase.download(user, id);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(invoice.getMimeType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + invoice.getFilename() + "\"")
        .body(resource);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      User user,
      @PathVariable UUID id
  ) {
    deleteInvoiceUseCase.delete(user, id);
  }
}