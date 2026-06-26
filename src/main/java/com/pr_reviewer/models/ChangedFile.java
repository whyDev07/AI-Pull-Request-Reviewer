package com.pr_reviewer.models;

public record ChangedFile(

        String fileName,
        String status,
        Integer additions,
        Integer deletions,
        String patch

) {}