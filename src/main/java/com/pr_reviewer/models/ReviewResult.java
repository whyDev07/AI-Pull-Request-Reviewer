package com.pr_reviewer.models;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public record ReviewResult(
        String summary,
        List<AiReviewComment> comments) { }