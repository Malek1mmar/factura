package com.example.fatoura.infrastructure.web.controller;

import com.example.fatoura.core.application.port.inbound.UploadInvoiceUseCase;
import com.example.fatoura.core.domain.model.Invoice;
import com.example.fatoura.core.domain.model.User;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

  private final UploadInvoiceUseCase uploadInvoiceUseCase;

  @PostMapping(
      value = "/upload",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public Invoice upload(
      User user,
      @RequestParam UUID organizationId,
      @RequestParam("file") MultipartFile file
  ) throws IOException {

    return uploadInvoiceUseCase.upload(
        user,
        organizationId,
        file
    );
  }
}