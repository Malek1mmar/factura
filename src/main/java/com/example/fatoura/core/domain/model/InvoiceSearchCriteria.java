package com.example.fatoura.core.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSearchCriteria {
  private UUID organizationId;
  private String supplierName;
  private String invoiceNumber;
  private InvoiceStatus status;
  private BigDecimal minAmount;
  private BigDecimal maxAmount;
  private LocalDate startDate;
  private LocalDate endDate;
}
