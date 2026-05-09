package com.example.fatoura.infrastructure.web.dto;

import com.example.fatoura.core.domain.model.InvoiceStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
  private UUID id;
  private String filename;
  private String mimeType;
  private Long fileSize;
  private InvoiceStatus status;
  private Instant uploadedAt;
}
