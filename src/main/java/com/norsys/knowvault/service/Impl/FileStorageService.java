package com.norsys.knowvault.service.Impl;

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
public class FileStorageService {

    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public Resource loadFile(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        return new UrlResource(filePath.toUri());
    }
}
