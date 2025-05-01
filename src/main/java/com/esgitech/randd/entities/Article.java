package com.esgitech.randd.entities;

import com.esgitech.randd.enums.ArticleStatus;
import com.esgitech.randd.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles")
@Builder
@Data
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is required")
    @NotNull
    private String title;

    @Column(name = "pdf_file_name")
    private String pdfFileName;

    private String doi;

    @NotBlank(message = "content is required")
    @NotNull
    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status = ArticleStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String tag;

    @ManyToOne
    @JoinColumn(name="users_id", nullable=false)
    @JsonIgnore
    private User user;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdfData;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "article_domaine",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "domaine_id")
    )
    private Set<Domaine> domaines = new HashSet<>();

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", category=" + category +
                '}';
    }
}
