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
//    private void appendOutputFormat(StringBuilder prompt) {
//
//        prompt.append("""
//        ## Response Requirements
//        Return ONLY a valid JSON object.
//        Do NOT use markdown.
//        Do NOT wrap the response with ```json.
//        Do NOT add explanations.
//        Do NOT add text before or after the JSON.
//        Do NOT add fields that are not present in the schema.
//
//        Use this EXACT schema:
//        {
//          "summary": "Overall review summary",
//          "comments": [
//            {
//              "fileName": "",
//              "lineNumber": 0,
//              "severity": "LOW",
//              "category": "BUG",
//              "comment": "",
//              "suggestion": ""
//            }
//          ]
//        }
//
//        If no issues are found:
//        {
//          "summary": "No issues found.",
//          "comments": []
//        }
//        """);
//    }

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
                The response MUST be a SINGLE valid JSON object.
                The "summary" field is REQUIRED.
                The "comments" field is REQUIRED.
                If there are no issues:
                 {
                   "summary": "No issues found.",
                   "comments": []
                 }
                 Allowed severity values ONLY:
                 LOW
                 MEDIUM
                 HIGH
                 CRITICAL
                 Allowed category values ONLY:
                 BUG
                 SECURITY
                 PERFORMANCE
                 STYLE
                 MAINTAINABILITY
                 Never use any other values.
                 Return JSON only.
              
                 Do not wrap in markdown.
                 Never wrap with ```json.
                 Do not explain anything.
                 The response MUST exactly match this schema:
                 The "summary" field is REQUIRED.
                 Never omit it.
                 Even if no issues are found, return:
                 {
                   "summary": "No issues found.",
                   "comments": []
                 }
                 If issues are found, you MUST still include the "summary" field.
               """);
    }
}