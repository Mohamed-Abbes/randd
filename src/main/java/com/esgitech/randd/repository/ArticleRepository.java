package com.esgitech.randd.repository;

import com.esgitech.randd.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findByTitleContainingOrContentContaining(String title, String content);
}
