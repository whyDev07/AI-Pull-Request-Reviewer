package com.pr_reviewer.repository;

import com.pr_reviewer.entity.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepo extends JpaRepository<PullRequest, Long> {

}
