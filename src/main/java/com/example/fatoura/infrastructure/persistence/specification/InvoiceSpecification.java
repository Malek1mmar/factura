package com.example.fatoura.infrastructure.persistence.specification;

import com.example.fatoura.core.domain.model.InvoiceSearchCriteria;
import com.example.fatoura.infrastructure.persistence.entity.InvoiceEntity;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceSpecification {

  public static Specification<InvoiceEntity> withCriteria(InvoiceSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (criteria.getOrganizationId() != null) {
        predicates.add(criteriaBuilder.equal(root.get("organization").get("id"), criteria.getOrganizationId()));
      }

      if (criteria.getSupplierName() != null && !criteria.getSupplierName().isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("supplierName")),
            "%" + criteria.getSupplierName().toLowerCase() + "%"));
      }

      if (criteria.getInvoiceNumber() != null && !criteria.getInvoiceNumber().isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("invoiceNumber"), criteria.getInvoiceNumber()));
      }

      if (criteria.getStatus() != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
      }

      if (criteria.getMinAmount() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"), criteria.getMinAmount()));
      }

      if (criteria.getMaxAmount() != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"), criteria.getMaxAmount()));
      }

      if (criteria.getStartDate() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("invoiceDate"), criteria.getStartDate()));
      }

      if (criteria.getEndDate() != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("invoiceDate"), criteria.getEndDate()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
