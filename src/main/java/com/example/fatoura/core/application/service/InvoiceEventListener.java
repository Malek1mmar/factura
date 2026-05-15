package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.ProcessInvoiceUseCase;
import com.example.fatoura.core.domain.event.InvoiceUploadedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceEventListener {

  private final ProcessInvoiceUseCase processInvoiceUseCase;

  @Async
  @EventListener
  public void handleInvoiceUploaded(InvoiceUploadedEvent event) {
    processInvoiceUseCase.process(event.invoiceId());
  }
}
