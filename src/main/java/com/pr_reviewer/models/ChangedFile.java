package com.pr_reviewer.models;

public record ChangedFile(

        String fileName,
        String patch,
        Integer additions,
        Integer deletions

) {}