package com.pr_reviewer.repository;

import com.pr_reviewer.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepo extends JpaRepository<ReviewComment, Long> {
}
