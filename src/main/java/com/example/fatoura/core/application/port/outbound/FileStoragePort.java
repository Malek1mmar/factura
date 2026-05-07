package com.example.fatoura.core.application.port.outbound;

import java.io.IOException;

public interface FileStoragePort {
  String store(byte[] content, String originalFilename, String contentType) throws IOException;
}
