package com.pr_reviewer.integration.github.dto;

public record GithubBranchRef(

        String ref,//branch name, reference
        String sha//Secure Hash Algorithm Hash ,unique id for every github commit

) {
}
