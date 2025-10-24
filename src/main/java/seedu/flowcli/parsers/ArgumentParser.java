package seedu.flowcli.parsers;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

/**
 * Lightweight parser for commands that begin with a project index argument.
 * Accepts only integer indices (1-based) and returns the referenced project if
 * it exists. Any remaining text after the index is preserved for further
 * command-specific parsing.
 */
public class ArgumentParser {

    private final String argument;
    private final ProjectList projects;

    private Project targetProject;
    private String remainingArgument;
    private String parsedProjectToken;
    private Integer targetProjectIndex;

    public ArgumentParser(String argument, ProjectList projects) {
        this.argument = argument == null ? "" : argument;
        this.projects = projects;
        parseArgument();
    }

    public Project getTargetProject() {
        return targetProject;
    }

    public String getRemainingArgument() {
        return remainingArgument;
    }

    public String getParsedProjectName() {
        return parsedProjectToken;
    }

    public Integer getTargetProjectIndex() {
        return targetProjectIndex;
    }

    private void parseArgument() {
        targetProject = null;
        remainingArgument = null;
        parsedProjectToken = null;
        targetProjectIndex = null;

        String trimmed = argument.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        String[] parts = trimmed.split("\\s+", 2);
        String firstToken = parts[0];
        parsedProjectToken = firstToken;

        Integer parsedIndex = tryParsePositiveInt(firstToken);
        if (parsedIndex != null) {
            int zeroBased = parsedIndex - 1;
            targetProjectIndex = zeroBased;
            if (zeroBased >= 0 && zeroBased < projects.getProjectListSize()) {
                targetProject = projects.getProjectByIndex(zeroBased);
            }
            remainingArgument = parts.length > 1 ? parts[1] : null;
            return;
        }

        // Non-numeric identifiers are preserved in remainingArgument for
        // commands
        // that do their own parsing (e.g. create-project).
        remainingArgument = trimmed.length() > firstToken.length() ? trimmed.substring(firstToken.length()).trim()
                : null;
    }

    private Integer tryParsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed > 0) {
                return parsed;
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
    }
}
