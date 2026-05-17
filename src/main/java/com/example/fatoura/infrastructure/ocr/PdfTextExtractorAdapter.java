package com.example.fatoura.infrastructure.ocr;

import com.example.fatoura.core.application.port.outbound.RawTextExtractorPort;
import com.example.fatoura.core.domain.constant.MessageConstants;
import com.example.fatoura.infrastructure.constant.InfrastructureConstants;
import com.example.fatoura.infrastructure.exception.InfrastructureException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PdfTextExtractorAdapter
    implements RawTextExtractorPort {

  @Override
  public String extract(String filePath) {
    try (PDDocument document = Loader.loadPDF(new File(filePath))) {
      PDFTextStripper stripper = new PDFTextStripper();
      return stripper.getText(document);
    } catch (IOException e) {
      throw new InfrastructureException(String.format(MessageConstants.OCR_EXTRACT_ERROR, filePath), e);
    }
  }

  @Override
  public boolean supports(String mimeType) {
    return InfrastructureConstants.MIME_PDF.equalsIgnoreCase(mimeType);
  }
}
