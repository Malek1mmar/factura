package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.ExtractedInvoiceData;

public interface InvoiceParsingPort {
  ExtractedInvoiceData parse(String rawText);
}
