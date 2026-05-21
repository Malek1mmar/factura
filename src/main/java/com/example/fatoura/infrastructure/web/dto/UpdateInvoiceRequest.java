package com.example.fatoura.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateInvoiceRequest {
  private String supplierName;
  private String invoiceNumber;
  private BigDecimal totalAmount;
  private LocalDate invoiceDate;
  private String currency;
}
