package com.esgitech.randd.service;

import com.esgitech.randd.dtos.ArticleDTO;
import com.esgitech.randd.dtos.Response;

public interface ArticleService {
    Response createArticle (ArticleDTO articleDTO);
    Response getAllArticles ();
    Response getArticleById (Long id);
    Response updateArticle (Long id, ArticleDTO articleDTO);
    Response deleteArticle (Long id);
    Response searchArticle(String input);

}
