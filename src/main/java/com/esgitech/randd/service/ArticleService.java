package com.esgitech.randd.service;

import com.esgitech.randd.dtos.ArticleDTO;
import com.esgitech.randd.dtos.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ArticleService {
    Response createArticle (ArticleDTO articleDTO) throws IOException;
    Response getAllArticles ();
    Response getArticleById (Long id);
    Response updateArticle (Long id, ArticleDTO articleDTO);
    Response deleteArticle (Long id);
    Response searchArticle(String input);
//    Response attachPdfToArticle(Long articleId, MultipartFile file) throws IOException;
    Response attachPdfToArticle(Long articleId, MultipartFile file) throws IOException;



}
