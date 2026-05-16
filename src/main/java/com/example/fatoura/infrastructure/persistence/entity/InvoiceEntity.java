package com.example.fatoura.infrastructure.persistence.entity;

import com.example.fatoura.core.domain.model.InvoiceStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceEntity {

  @Id
  @GeneratedValue
  private UUID id;

  private String filename;

  private String storagePath;

  private String mimeType;

  private Long fileSize;

  @Enumerated(EnumType.STRING)
  private InvoiceStatus status;

  private Instant uploadedAt;

  @ManyToOne(optional = false)
  private OrganizationEntity organization;

  @ManyToOne(optional = false)
  private UserEntity uploadedBy;

  @jakarta.persistence.Column(columnDefinition = "TEXT")
  private String rawContent;

  private String supplierName;
  private String invoiceNumber;
  private BigDecimal totalAmount;
  private LocalDate invoiceDate;
  private String currency;
}