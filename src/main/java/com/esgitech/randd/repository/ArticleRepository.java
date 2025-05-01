package com.esgitech.randd.repository;

import com.esgitech.randd.entities.Article;
import com.esgitech.randd.enums.ArticleStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrDoiContainingIgnoreCase(String title, String content, String doi);
    List<Article> findByStatus(ArticleStatus status, Sort sort);
    List<Article> findByStatusAndTitleContainingIgnoreCaseOrStatusAndContentContainingIgnoreCaseOrStatusAndDoiContainingIgnoreCase(ArticleStatus articleStatus, String input, ArticleStatus articleStatus1, String input1, ArticleStatus articleStatus2, String input2);
}
