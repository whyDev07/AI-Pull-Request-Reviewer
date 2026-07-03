package com.pr_reviewer.repository;

import com.pr_reviewer.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByPullRequestId(Long pullRequestId);
}
