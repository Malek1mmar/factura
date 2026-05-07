package com.example.fatoura.infrastructure.storage;

import com.example.fatoura.core.application.port.outbound.FileStoragePort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

  private static final String UPLOAD_DIR = "uploads/";

  @Override
  public String store(byte[] content, String originalFilename, String contentType) throws IOException {

    String filename = UUID.randomUUID() + "_" + originalFilename;

    Path path = Paths.get(UPLOAD_DIR + filename);

    Files.createDirectories(path.getParent());

    Files.write(path, content);

    return path.toString();
  }
}
