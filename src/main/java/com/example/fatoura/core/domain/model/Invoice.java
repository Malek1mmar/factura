package com.example.fatoura.core.domain.model;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
  private UUID id;
  private String filename;
  private String storagePath;
  private String mimeType;
  private Long fileSize;
  private InvoiceStatus status;
  private Instant uploadedAt;
  private Organization organization;
  private User uploadedBy;
}
