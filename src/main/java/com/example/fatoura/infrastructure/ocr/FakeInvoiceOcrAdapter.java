package com.example.fatoura.infrastructure.ocr;

import com.example.fatoura.core.application.port.outbound.InvoiceOcrPort;
import com.example.fatoura.core.domain.model.ExtractedInvoiceData;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class FakeInvoiceOcrAdapter implements InvoiceOcrPort {

  @Override
  public ExtractedInvoiceData process(String filePath) {
    return ExtractedInvoiceData.builder()
        .supplierName("Carrefour")
        .invoiceNumber("FAKE-001")
        .totalAmount(new BigDecimal("240.50"))
        .currency("TND")
        .invoiceDate(LocalDate.now())
        .build();
  }
}
