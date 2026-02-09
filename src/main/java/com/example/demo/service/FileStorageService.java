package com.example.demo.service;

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

    /**
     * Save a file to the specified directory path
     * 
     * @param file       The file to upload
     * @param uploadPath The directory path where file should be saved (e.g.,
     *                   "uploads/products")
     * @return The unique filename that was saved
     * @throws IOException if file cannot be saved
     */
    public String saveFile(MultipartFile file, String uploadPath) throws IOException {
        // Create upload directory if it doesn't exist
        Path directoryPath = Paths.get(uploadPath);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Generate unique filename with original extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = directoryPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    /**
     * Delete a file from the specified directory
     * 
     * @param filename   The name of the file to delete
     * @param uploadPath The directory path where file is located
     * @return true if file was deleted, false otherwise
     */
    public boolean deleteFile(String filename, String uploadPath) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filename + " from " + uploadPath);
            return false;
        }
    }

    /**
     * Get the full path to a file
     * 
     * @param filename   The name of the file
     * @param uploadPath The directory path where file is located
     * @return Path object representing the file location
     */
    public Path getFilePath(String filename, String uploadPath) {
        return Paths.get(uploadPath).resolve(filename);
    }

    /**
     * Check if a file exists
     * 
     * @param filename   The name of the file
     * @param uploadPath The directory path where file should be located
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filename, String uploadPath) {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        return Files.exists(filePath);
    }
}
