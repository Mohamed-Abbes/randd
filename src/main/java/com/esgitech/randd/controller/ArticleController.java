package com.esgitech.randd.controller;

import com.esgitech.randd.dtos.ArticleDTO;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.entities.Article;
import com.esgitech.randd.repository.ArticleRepository;
import com.esgitech.randd.service.ArticleService;
import com.esgitech.randd.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final FileStorageService fileStorageService;


    @GetMapping("/download-pdf/{articleId}")
    public ResponseEntity<Resource> downloadArticlePdf(@PathVariable Long articleId) throws IOException {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (articleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Article article = articleOptional.get();
        if (article.getPdfFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = fileStorageService.loadFile(article.getPdfFileName());
        Resource resource = new UrlResource(filePath.toUri());
        System.out.println("Loading file from path: " + filePath.toString());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload-pdf/{articleId}")
    public ResponseEntity<?> uploadArticlePdf(@PathVariable Long articleId,
                                              @RequestParam("file") MultipartFile file) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (articleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed.");
        }

        try {
            // Define directory
            String uploadDir = "upload/pdf";
            // Ensure directory exists
            Files.createDirectories(Paths.get(uploadDir));

            // Build file path
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // Unique filename
            Path filePath = Paths.get(uploadDir, fileName);

            // Save file to disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update the article with the PDF filename
            Article article = articleOptional.get();
            article.setPdfFileName(fileName); // assuming you have this field
            articleRepository.save(article);

            return ResponseEntity.ok(Collections.singletonMap("message", "PDF uploaded successfully."));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading PDF: " + e.getMessage());
        }
    }


    //Tested and validated
    @PostMapping
    public ResponseEntity<Response> addArticle( @RequestBody ArticleDTO articleDTO) throws IOException {
        System.out.println(articleDTO);
        return ResponseEntity.ok(articleService.createArticle(articleDTO));
    }
    //Tested and validated
    @GetMapping("/{id}")
    public ResponseEntity<Response> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    //Tested and validated
    @GetMapping("/all")
    public ResponseEntity<Response> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchArticle(@RequestParam String input) {
        return ResponseEntity.ok(articleService.searchArticle(input));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.ok(articleService.updateArticle(id,articleDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteArticle( @PathVariable Long id) {
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }








}
