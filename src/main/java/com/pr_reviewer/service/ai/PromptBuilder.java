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
        You are a Senior Java Backend Engineer performing a professional Pull Request review.
        Your responsibility is to review ONLY the code provided.
        Never assume the existence or contents of files that are not included.
        If there is insufficient context to determine whether something is an issue, do not report it.""");
    }

    private void appendTask(StringBuilder prompt) {

        prompt.append("""
        ## Task
        Review the modified code for:
        - Bugs
        - Security Issues
        - Performance Problems
        - Maintainability
        - Readability
        - Best Practices
        - Edge Cases
        Report only real problems.
        """);
    }

    private void appendRules(StringBuilder prompt) {
        prompt.append("""
        ## Rules
        - Review ONLY the supplied diff.
        - Do NOT guess missing code.
        - Do NOT invent issues.
        - Ignore formatting unless it affects readability.
        - Do not report duplicate issues.
        - Explain WHY each issue is a problem.
        - Provide a practical suggestion.
        - If no issues exist, return an empty comments array.
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
            ## OUTPUT
            Return ONLY ONE JSON object.
            Do not write markdown.
            Do not write explanations.
            Do not write ```json.

            The JSON MUST EXACTLY match this schema.
            {
              "summary": "Overall review summary",
              "comments": [
                {
                  "fileName": "src/File.java",
                  "lineNumber": 10,
                  "severity": "LOW",
                  "category": "BUG",
                  "comment": "Explain the problem.",
                  "suggestion": "Explain the fix."
                }
              ]
            }

            Rules:
            - summary is REQUIRED.
            - comments is REQUIRED.
            - If there are no issues:
              {
                "summary":"No issues found.",
                "comments":[]
              }

            Allowed severity values:
            LOW
            MEDIUM
            HIGH
            CRITICAL

            Allowed category values:
            BUG
            SECURITY
            PERFORMANCE
            STYLE
            MAINTAINABILITY

            NEVER rename any field.
            NEVER invent new field names.
            NEVER use:
            file
            line
            issue
            explanation

            ALWAYS use:
            fileName
            lineNumber
            comment
            suggestion

            """);
    }
}