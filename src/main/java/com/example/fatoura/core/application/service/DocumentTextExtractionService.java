package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.outbound.RawTextExtractorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentTextExtractionService {

  private final List<RawTextExtractorPort> extractors;

  public String extract(
      String filePath,
      String mimeType
  ) {
    return extractors.stream()
        .filter(extractor -> extractor.supports(mimeType))
        .findFirst()
        .map(extractor -> extractor.extract(filePath))
        .orElseThrow(() -> new IllegalArgumentException("No extractor found for mime type: " + mimeType));
  }
}