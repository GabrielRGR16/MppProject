package com.myapp.PlayerManager.controller;

import com.myapp.PlayerManager.model.FileEntity;
import com.myapp.PlayerManager.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final FileRepository fileRepository;

    @Autowired
    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GetMapping
    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileContent(file.getBytes());
            fileEntity.setContentType(file.getContentType()); // Set MIME type (e.g., "video/mp4")
            fileRepository.save(fileEntity);
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        FileEntity fileEntity = fileRepository.findById(id).orElse(null);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(fileEntity.getFileContent());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(fileEntity.getContentType() != null ? fileEntity.getContentType() : "application/octet-stream"))
                .contentLength(fileEntity.getFileContent().length)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        if (!fileRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found with id: " + id);
        }
        fileRepository.deleteById(id);
        return ResponseEntity.ok("File deleted successfully: " + id);
    }
}