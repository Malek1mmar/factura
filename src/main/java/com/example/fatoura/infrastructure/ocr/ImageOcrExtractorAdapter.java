package com.example.fatoura.infrastructure.ocr;

import com.example.fatoura.core.application.port.outbound.RawTextExtractorPort;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ImageOcrExtractorAdapter
    implements RawTextExtractorPort {

  private static final Set<String> SUPPORTED_MIME_TYPES = Set.of(
      "image/jpeg",
      "image/png",
      "image/tiff",
      "image/bmp"
  );

  @Override
  public String extract(String filePath) {
    // This would typically use Tesseract OCR or a cloud service like AWS Textract
    // For now, we return a placeholder or throw an exception if not yet fully implemented
    return "OCR extraction not fully implemented for: " + filePath;
  }

  @Override
  public boolean supports(String mimeType) {
    return SUPPORTED_MIME_TYPES.contains(mimeType.toLowerCase());
  }
}