package com.pr_reviewer.service.ai;

import com.pr_reviewer.models.ChangedFile;
import com.pr_reviewer.models.PullRequestDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptBuilder {

    public String buildPrompt(
            PullRequestDetails details,
            List<ChangedFile> files
    ) {
        StringBuilder prompt = new StringBuilder();

        appendHeader(prompt);
        appendTask(prompt);
        appendRules(prompt);
        appendMetadata(prompt, details);
        appendChangedFiles(prompt, files);
        appendOutputFormat(prompt);

        return prompt.toString();
    }

    private void appendHeader(StringBuilder prompt) {
        prompt.append("""
                You are an experienced Senior Java Backend Engineer.
                Review the following Pull Request like a professional code reviewer.
                """);
    }

    private void appendTask(StringBuilder prompt) {
        prompt.append("""
                ## Task
                Analyze the code for:
                - Bugs
                - Security Issues
                - Performance Issues
                - Readability
                - Maintainability
                - Best Practices
                - Possible Edge Cases
                """);
    }

    private void appendRules(StringBuilder prompt) {
        prompt.append("""
                ## Rules
                - Do not invent issues.
                - Only review modified code.
                - Avoid duplicate comments.
                - Keep comments concise.
                - Explain why something is an issue.
                - Suggest an improved implementation.
                - Return ONLY valid JSON.
                """);
    }

    private void appendMetadata(
            StringBuilder prompt,
            PullRequestDetails details
    ) {

        prompt.append("""
                ## Pull Request Information
                """);
        prompt.append("Title: ")
                .append(details.title())
                .append("\n");
        prompt.append("State: ")
                .append(details.state())
                .append("\n");
        prompt.append("Source Branch: ")
                .append(details.sourceBranch())
                .append("\n");
        prompt.append("Target Branch: ")
                .append(details.targetBranch())
                .append("\n");
        prompt.append("Commit SHA: ")
                .append(details.commitSha())
                .append("\n\n");
    }

    private void appendChangedFiles(
            StringBuilder prompt,
            List<ChangedFile> files
    ) {
        prompt.append("""
                ## Changed Files

                """);

        for (ChangedFile file : files) {
            prompt.append("----------------------------------------\n");
            prompt.append("File: ")
                    .append(file.fileName())
                    .append("\n");

            prompt.append("Status: ")
                    .append(file.status())
                    .append("\n");

            prompt.append("Additions: ")
                    .append(file.additions())
                    .append("\n");

            prompt.append("Deletions: ")
                    .append(file.deletions())
                    .append("\n\n");

            prompt.append("Diff:\n");

            prompt.append(file.patch() == null ? "No diff available." : file.patch());

            prompt.append("\n\n");
        }
    }

    private void appendOutputFormat(StringBuilder prompt) {

        prompt.append("""
                ## Expected JSON Response
                {
                  "summary": "Overall review summary",
                  "comments": [
                    {
                      "fileName": "",
                      "lineNumber": 0,
                      "severity": "LOW | MEDIUM | HIGH | CRITICAL",
                      "category": "BUG | SECURITY | PERFORMANCE | STYLE | MAINTAINABILITY",
                      "comment": "",
                      "suggestion": ""
                    }
                  ]
                }
                Return ONLY the JSON object.
                Do not include markdown.
                Do not include explanations outside the JSON.

                """);
    }
}