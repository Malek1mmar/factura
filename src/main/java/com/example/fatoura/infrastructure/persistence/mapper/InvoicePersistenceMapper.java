package com.example.fatoura.infrastructure.persistence.mapper;

import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.infrastructure.persistence.entity.InvoiceEntity;

public class InvoicePersistenceMapper {

  public static Invoice toDomain(InvoiceEntity entity) {
    if (entity == null) return null;
    return Invoice.builder()
        .id(entity.getId())
        .filename(entity.getFilename())
        .storagePath(entity.getStoragePath())
        .mimeType(entity.getMimeType())
        .fileSize(entity.getFileSize())
        .status(entity.getStatus())
        .uploadedAt(entity.getUploadedAt())
        .organization(OrganizationPersistenceMapper.toDomain(entity.getOrganization()))
        .uploadedBy(UserPersistenceMapper.toDomain(entity.getUploadedBy()))
        .build();
  }

  public static InvoiceEntity toEntity(Invoice domain) {
    if (domain == null) return null;
    return InvoiceEntity.builder()
        .id(domain.getId())
        .filename(domain.getFilename())
        .storagePath(domain.getStoragePath())
        .mimeType(domain.getMimeType())
        .fileSize(domain.getFileSize())
        .status(domain.getStatus())
        .uploadedAt(domain.getUploadedAt())
        .organization(OrganizationPersistenceMapper.toEntity(domain.getOrganization()))
        .uploadedBy(UserPersistenceMapper.toEntity(domain.getUploadedBy()))
        .build();
  }
}
