package com.esgitech.randd.service.impl;

import com.esgitech.randd.dtos.ArticleDTO;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.entities.Article;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.exception.NotFoundException;
import com.esgitech.randd.repository.ArticleRepository;
import com.esgitech.randd.repository.UserRepository;
import com.esgitech.randd.security.JwtUtils;
import com.esgitech.randd.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository   userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Transactional
    @Override
    public Response createArticle(ArticleDTO articleDTO) {
        if (articleDTO.getUser() == null || articleDTO.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }
        User user = userRepository.findById(articleDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article articleToSave = Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .category(articleDTO.getCategory())
                .user(user)
                .build();

        articleRepository.save(articleToSave);
        return Response.builder()
                .status(201)
                .message("Article successfully created")
                .build();
    }

    @Override
    public Response getAllArticles() {
        List<Article> articles = articleRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));

        List<ArticleDTO> articlesDTOS = getViewArticleDTOS(articles);
        return Response.builder()
                .status(200)
                .articles(articlesDTOS)
                .message("success")
                .build();
    }

    @Override
    public Response getArticleById(Long id) {
        Article article = getArticleWithException(id);

        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        return Response.builder()
                .status(200)
                .message("Article found successfully")
                .article(articleDTO)
                .build();
    }

    @Override
    public Response updateArticle(Long id, ArticleDTO articleDTO) {
        System.out.println(articleDTO);

        Article existingArticle = getArticleWithException(id);
        if(articleDTO.getTitle() != null && !articleDTO.getTitle().isBlank()) {
            existingArticle.setTitle(articleDTO.getTitle());
        }
        if(articleDTO.getContent() != null && !articleDTO.getContent().isBlank()) {
            existingArticle.setContent(articleDTO.getContent());
        }
        if(articleDTO.getCategory() != null) {
            existingArticle.setCategory(articleDTO.getCategory());
        }
        articleRepository.save(existingArticle);
        return Response.builder()
                .status(200)
                .message("Article updated successfully")
                .build();
    }

    @Override
    public Response deleteArticle(Long id) {
        Article existingArticle = getArticleWithException(id);
        articleRepository.delete(existingArticle);

        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public Response searchArticle(String input) {
        List<Article> articlesList = articleRepository.findByTitleContainingOrContentContaining(input,input);

        if(articlesList.isEmpty()){
            throw new NotFoundException("Article not found");
        }

        List<ArticleDTO> articlesDTOList = getViewArticleDTOS(articlesList);

        return Response.builder()
                .status(200)
                .message("Success")
                .articles(articlesDTOList)
                .build();
    }

    private Article getArticleWithException(Long id) {
        Article existingArticle = articleRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Article not found"));
        return existingArticle;
    }

    private List<ArticleDTO> getViewArticleDTOS(List<Article> articlesList) {
        List<ArticleDTO> articlesDTOList = modelMapper.map(articlesList, new TypeToken<List<ArticleDTO>>() {}.getType());
        articlesDTOList.forEach(articleDTO -> {
            articleDTO.getUser().setPassword(null);
            articleDTO.getUser().setArticles(null);
            articleDTO.getUser().setRole(null);
            articleDTO.getUser().setCreatedAt(null);
        });
        return articlesDTOList;
    }
}
