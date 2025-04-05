package com.esgitech.randd.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.storage.location}") String fileStorageLocation) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public Path loadFile(String fileName) {
        return fileStorageLocation.resolve(fileName).normalize();
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = loadFile(fileName);
        Files.deleteIfExists(filePath);
    }
}