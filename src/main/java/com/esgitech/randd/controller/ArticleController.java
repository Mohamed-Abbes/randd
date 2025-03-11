package com.esgitech.randd.controller;

import com.esgitech.randd.dtos.ArticleDTO;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<Response> addArticle(@RequestBody ArticleDTO  articleDTO) {
        return ResponseEntity.ok(articleService.createArticle(articleDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

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
