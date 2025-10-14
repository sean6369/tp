package seedu.flowcli.parsers;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

public class ArgumentParser {

    private String argument;
    private ProjectList projects;

    private Project targetProject;
    private String remainingArgument;

    public ArgumentParser(String argument, ProjectList projects) {
        this.argument = argument;
        this.projects = projects;
        parseArgument();

    }

    public Project getTargetProject() {
        return targetProject;
    }

    public String getRemainingArgument() {
        return remainingArgument;
    }

    public void parseArgument() {
        if (argument.isEmpty()) {
            targetProject = null;
            remainingArgument = null;
            return;
        }

        String trimmed = argument.trim();
        if (trimmed.isEmpty()) {
            targetProject = null;
            remainingArgument = null;
            return;
        }

        ParsedProject parsedProject = parseProjectIdentifier(trimmed);
        String projectIdentifier = parsedProject.projectName;
        String arguments = parsedProject.remainingArguments;

        Project existingProject = projects.getProject(projectIdentifier);
        if (existingProject != null) {
            targetProject = existingProject;
            remainingArgument = arguments;
            return;
        }

        targetProject = null;
        if (arguments == null || arguments.isEmpty()) {
            remainingArgument = projectIdentifier;
        } else if (projectIdentifier.isEmpty()) {
            remainingArgument = arguments;
        } else {
            remainingArgument = projectIdentifier + " " + arguments;
        }

    }

    private ParsedProject parseProjectIdentifier(String input) {
        if (!input.startsWith("\"")) {
            String[] parts = input.split("\\s+", 2);
            String projectName = parts[0].trim();
            String arguments = parts.length > 1 ? parts[1].trim() : null;
            return new ParsedProject(projectName, arguments);
        }

        StringBuilder builder = new StringBuilder();
        int index = 1;
        while (index < input.length()) {
            char current = input.charAt(index);
            if (current == '\\' && index + 1 < input.length()) {
                char next = input.charAt(index + 1);
                if (next == '"' || next == '\\') {
                    builder.append(next);
                    index += 2;
                    continue;
                }
            }
            if (current == '"') {
                index++;
                break;
            }
            builder.append(current);
            index++;
        }

        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }

        String remaining = index < input.length() ? input.substring(index).trim() : null;
        return new ParsedProject(builder.toString().trim(), remaining);
    }

    private static class ParsedProject {
        private final String projectName;
        private final String remainingArguments;

        private ParsedProject(String projectName, String remainingArguments) {
            this.projectName = projectName;
            this.remainingArguments = remainingArguments;
        }
    }
}
