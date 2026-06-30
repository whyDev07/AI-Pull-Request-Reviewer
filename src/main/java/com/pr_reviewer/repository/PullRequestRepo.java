package com.pr_reviewer.repository;

import com.pr_reviewer.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PullRequestRepo extends JpaRepository<PullRequest, Long> {
    //Every Review request shouldn't create a duplicate pullRequest..
    Optional<PullRequest> findByGithubPrId(Long githubPrId);
}
