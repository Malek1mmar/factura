package com.example.fatoura.core.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedInvoiceData {

  private String supplierName;
  private String invoiceNumber;
  private BigDecimal totalAmount;
  private LocalDate invoiceDate;
  private String currency;
}
