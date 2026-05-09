package com.example.fatoura.infrastructure.web.mapper;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.infrastructure.web.dto.InvoiceResponse;

public class InvoiceWebMapper {

  public static InvoiceResponse toResponse(Invoice invoice) {
    if (invoice == null) return null;
    return InvoiceResponse.builder()
        .id(invoice.getId())
        .filename(invoice.getFilename())
        .mimeType(invoice.getMimeType())
        .fileSize(invoice.getFileSize())
        .status(invoice.getStatus())
        .uploadedAt(invoice.getUploadedAt())
        .build();
  }
}
