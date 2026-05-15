package com.example.fatoura.core.application.port.outbound;

public interface RawTextExtractorPort {
  String extract(String filePath);
  boolean supports(String mimeType);
}
