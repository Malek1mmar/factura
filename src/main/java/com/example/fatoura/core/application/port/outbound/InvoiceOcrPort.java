package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.ExtractedInvoiceData;

public interface InvoiceOcrPort {
  ExtractedInvoiceData process(String filePath);
}
