package com.norsys.knowvault.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@Slf4j
public class FileStorageService {

    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        log.info("Starting file upload - original filename: '{}', size: {} bytes",
                file.getOriginalFilename(), file.getSize());

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            log.debug("Generated filename: '{}', saving to path: {}", fileName, filePath);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Successfully saved file: '{}' to {}", fileName, filePath);
            return fileName;
        } catch (IOException e) {
            log.error("Failed to save file '{}': {}", file.getOriginalFilename(), e.getMessage(), e);
            throw e;
        }
    }

    public Resource loadFile(String fileName) throws MalformedURLException {
        log.debug("Loading file: '{}'", fileName);

        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                log.debug("Successfully loaded file: '{}'", fileName);
            } else {
                log.warn("File not found: '{}'", fileName);
            }

            return resource;
        } catch (MalformedURLException e) {
            log.error("Malformed URL when loading file '{}': {}", fileName, e.getMessage(), e);
            throw e;
        }
    }
}
