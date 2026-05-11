package com.example.fatoura.core.application.port.outbound;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface FileStoragePort {
  String store(byte[] content, String originalFilename, String contentType) throws IOException;
  Resource loadAsResource(String path);
}
