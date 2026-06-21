package com.pr_reviewer.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pull_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long githubPrId;

    @Column(nullable = false)
    private Long prNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String repoOwner;

    @Column(nullable = false)
    private String repoName;

    @Column(nullable = false)
    private String sourceBranch;

    @Column(nullable = false)
    private String targetBranch;

    @Column(nullable = false)
    private String commitSha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PullRequestStatus prStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "pullRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Review> reviews = new ArrayList<>();
}