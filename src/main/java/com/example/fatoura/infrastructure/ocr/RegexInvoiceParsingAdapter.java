package com.example.fatoura.infrastructure.ocr;

import com.example.fatoura.core.application.port.outbound.InvoiceParsingPort;
import com.example.fatoura.core.domain.model.ExtractedInvoiceData;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class RegexInvoiceParsingAdapter implements InvoiceParsingPort {

  @Override
  public ExtractedInvoiceData parse(String rawText) {
    if (rawText == null || rawText.isEmpty()) {
      return new ExtractedInvoiceData();
    }

    return ExtractedInvoiceData.builder()
        .supplierName(extractSupplierName(rawText))
        .invoiceNumber(extractInvoiceNumber(rawText))
        .totalAmount(extractTotalAmount(rawText))
        .invoiceDate(extractInvoiceDate(rawText))
        .currency(extractCurrency(rawText))
        .build();
  }

  private String extractSupplierName(String rawText) {
    // Simple logic: first non-empty line usually contains the supplier name
    String[] lines = rawText.split("\\r?\\n");
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        return line.trim();
      }
    }
    return null;
  }

  private String extractInvoiceNumber(String rawText) {
    Pattern pattern = Pattern.compile("(?i)(?:invoice|facture|n°|no)\\s*(?:number|numéro|#)?[:\\s]*([A-Z0-9-]+)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(rawText);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  private BigDecimal extractTotalAmount(String rawText) {
    // Matches patterns like "Total 123.45" or "Montant TTC: 123,45"
    Pattern pattern = Pattern.compile("(?i)(?:total|montant|ttc|total\\s*ttc)[:\\s]*([0-9]{1,3}(?:[\\s,][0-9]{3})*[.,][0-9]{2,3})", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(rawText);
    String lastMatch = null;
    while (matcher.find()) {
      lastMatch = matcher.group(1);
    }

    if (lastMatch != null) {
      String amount = lastMatch.replace(" ", "").replace(",", ".");
      try {
        return new BigDecimal(amount);
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return null;
  }

  private LocalDate extractInvoiceDate(String rawText) {
    // Matches DD/MM/YYYY or YYYY-MM-DD
    Pattern pattern = Pattern.compile("(\\d{2}[/.-]\\d{2}[/.-]\\d{4})|(\\d{4}[/.-]\\d{2}[/.-]\\d{2})");
    Matcher matcher = pattern.matcher(rawText);
    if (matcher.find()) {
      String dateStr = matcher.group(0).replace(".", "-").replace("/", "-");
      try {
        if (dateStr.matches("\\d{2}-\\d{2}-\\d{4}")) {
          return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } else {
          return LocalDate.parse(dateStr);
        }
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  private String extractCurrency(String rawText) {
    if (rawText.contains("TND") || rawText.contains("DT")) return "TND";
    if (rawText.contains("EUR") || rawText.contains("€")) return "EUR";
    if (rawText.contains("USD") || rawText.contains("$")) return "USD";
    return "TND"; // Default for this project context
  }
}
