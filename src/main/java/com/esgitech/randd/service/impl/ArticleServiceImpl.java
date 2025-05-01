package com.esgitech.randd.service.impl;

import com.esgitech.randd.dtos.ArticleDTO;
import com.esgitech.randd.dtos.Response;
import com.esgitech.randd.entities.Article;
import com.esgitech.randd.enums.ArticleStatus;
import com.esgitech.randd.entities.User;
import com.esgitech.randd.exception.NotFoundException;
import com.esgitech.randd.repository.ArticleRepository;
import com.esgitech.randd.repository.UserRepository;
import com.esgitech.randd.security.JwtUtils;
import com.esgitech.randd.service.ArticleService;
import com.esgitech.randd.service.FileStorageService;
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
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final FileStorageService fileStorageService;

    @Transactional
    @Override
    public Response createArticle(ArticleDTO articleDTO) {
        if (articleDTO.getUser() == null || articleDTO.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }
        User user = userRepository.findById(articleDTO.getUser().getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Article articleToSave = Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .doi(articleDTO.getDoi())
                .category(articleDTO.getCategory())
                .user(user)
                .status(ArticleStatus.PENDING) // New articles should be PENDING by default
                .build();

        articleRepository.save(articleToSave);
        return Response.builder()
                .status(201)
                .message("Article successfully created and is pending approval")
                .build();
    }

    @Override
    public Response getAllArticles() {
        // Only return APPROVED articles for general listing
        List<Article> articles = articleRepository.findByStatus(ArticleStatus.APPROVED, Sort.by(Sort.Direction.ASC, "title"));
        List<ArticleDTO> articlesDTOS = getViewArticleDTOS(articles);
        return Response.builder()
                .status(200)
                .articles(articlesDTOS)
                .message("Success")
                .build();
    }

    @Override
    public Response getArticleById(Long id) {
        Article article = getArticleWithException(id);

        // Only allow access to APPROVED articles, or PENDING/REJECTED if user is the owner or admin
        //        // You'll need to implement proper authorization checks here
        // For now, we'll just return the article regardless of status
        // In a real application, you should check user permissions

        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        return Response.builder()
                .status(200)
                .message("Article found successfully")
                .article(articleDTO)
                .build();
    }

    @Override
    public Response updateArticle(Long id, ArticleDTO articleDTO) {
        Article existingArticle = getArticleWithException(id);

        // If article was approved/rejected, changing it should set status back to PENDING
        if (existingArticle.getStatus() != ArticleStatus.PENDING) {
            existingArticle.setStatus(ArticleStatus.PENDING);
            existingArticle.setRejectionReason(null); // Clear rejection reason if it exists
        }

        if (articleDTO.getTitle() != null && !articleDTO.getTitle().isBlank()) {
            existingArticle.setTitle(articleDTO.getTitle());
        }
        if (articleDTO.getContent() != null && !articleDTO.getContent().isBlank()) {
            existingArticle.setContent(articleDTO.getContent());
        }
        if (articleDTO.getCategory() != null) {
            existingArticle.setCategory(articleDTO.getCategory());
        }

        if (articleDTO.getTag() != null) {
            existingArticle.setTag(articleDTO.getTag());
        }

        articleRepository.save(existingArticle);
        return Response.builder()
                .status(200)
                .message("Article updated successfully and status set to pending")
                .build();
    }

    @Override
    public Response deleteArticle(Long id) {
        Article existingArticle = getArticleWithException(id);
        articleRepository.delete(existingArticle);
        return Response.builder()
                .status(200)
                .message("Article deleted successfully")
                .build();
    }

    @Override
    public Response searchArticle(String input) {
        // Only search through APPROVED articles for general search
        List<Article> articlesList = articleRepository.findByStatusAndTitleContainingIgnoreCaseOrStatusAndContentContainingIgnoreCaseOrStatusAndDoiContainingIgnoreCase(
                ArticleStatus.APPROVED, input,
                ArticleStatus.APPROVED, input,
                ArticleStatus.APPROVED, input);

        if (articlesList.isEmpty()) {
            throw new NotFoundException("Article not found");
        }
        List<ArticleDTO> articlesDTOList = getViewArticleDTOS(articlesList);
        return Response.builder()
                .status(200)
                .message("Success")
                .articles(articlesDTOList)
                .build();
    }

    @Override
    public Response getPendingArticles() {
        List<Article> articles = articleRepository.findByStatus(ArticleStatus.PENDING, Sort.by(Sort.Direction.ASC, "title"));
        List<ArticleDTO> articlesDTOS = getViewArticleDTOS(articles);
        return Response.builder()
                .status(200)
                .articles(articlesDTOS)
                .message("Pending articles retrieved successfully")
                .build();
    }

    @Override
    public Response getApprovedArticles() {
        List<Article> articles = articleRepository.findByStatus(ArticleStatus.APPROVED, Sort.by(Sort.Direction.ASC, "title"));
        List<ArticleDTO> articlesDTOS = getViewArticleDTOS(articles);
        return Response.builder()
                .status(200)
                .articles(articlesDTOS)
                .message("Approved articles retrieved successfully")
                .build();
    }

    @Override
    public Response getRejectedArticles() {
        List<Article> articles = articleRepository.findByStatus(ArticleStatus.REJECTED, Sort.by(Sort.Direction.ASC, "title"));
        List<ArticleDTO> articlesDTOS = getViewArticleDTOS(articles);
        return Response.builder()
                .status(200)
                .articles(articlesDTOS)
                .message("Rejected articles retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public Response approveArticle(Long id) {
        Article article = getArticleWithException(id);
        article.setStatus(ArticleStatus.APPROVED);
        article.setRejectionReason(null); // Clear any previous rejection reason
        articleRepository.save(article);
        return Response.builder()
                .status(200)
                .message("Article approved successfully")
                .build();
    }

    @Override
    @Transactional
    public Response rejectArticle(Long id, String reason) {
        Article article = getArticleWithException(id);
        article.setStatus(ArticleStatus.REJECTED);
        article.setRejectionReason(reason);
        articleRepository.save(article);
        return Response.builder()
                .status(200)
                .message("Article rejected successfully")
                .build();
    }

    private Article getArticleWithException(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found"));
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