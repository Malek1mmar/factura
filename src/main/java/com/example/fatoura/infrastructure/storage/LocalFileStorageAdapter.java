package com.example.fatoura.infrastructure.storage;

import com.example.fatoura.core.application.port.outbound.FileStoragePort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

  @Override
  public Resource loadAsResource(String path) {
    try {
      Path file = Paths.get(path);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read file: " + path);
      }
    } catch (Exception e) {
      throw new RuntimeException("Could not read file: " + path, e);
    }
  }

  @Override
  public void delete(String path) throws IOException {
    Path file = Paths.get(path);
    Files.deleteIfExists(file);
  }
}
