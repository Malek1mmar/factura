package com.example.fatoura.infrastructure.ocr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.fatoura.core.domain.model.ExtractedInvoiceData;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class RegexInvoiceParsingAdapterTest {

  private final RegexInvoiceParsingAdapter adapter = new RegexInvoiceParsingAdapter();

  @Test
  void shouldParseInvoiceTextSuccessfully() {
    String rawText = """
        Carrefour Market
        Facture N° INV-2024-001
        Date: 15/05/2026
        Total TTC: 150,50 TND
        """;

    ExtractedInvoiceData data = adapter.parse(rawText);

    assertNotNull(data);
    assertEquals("Carrefour Market", data.getSupplierName());
    assertEquals("INV-2024-001", data.getInvoiceNumber());
    assertEquals(new BigDecimal("150.50"), data.getTotalAmount());
    assertEquals(LocalDate.of(2026, 5, 15), data.getInvoiceDate());
    assertEquals("TND", data.getCurrency());
  }

  @Test
  void shouldParseAlternativeFormats() {
    String rawText = """
        STEG
        Invoice # 789456123
        Date: 2026-04-10
        Montant: 45.000 DT
        """;

    ExtractedInvoiceData data = adapter.parse(rawText);

    assertNotNull(data);
    assertEquals("STEG", data.getSupplierName());
    assertEquals("789456123", data.getInvoiceNumber());
    assertEquals(new BigDecimal("45.000"), data.getTotalAmount());
    assertEquals(LocalDate.of(2026, 4, 10), data.getInvoiceDate());
    assertEquals("TND", data.getCurrency());
  }
}
