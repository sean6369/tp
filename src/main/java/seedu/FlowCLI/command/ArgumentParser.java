package seedu.FlowCLI.command;

import seedu.FlowCLI.project.Project;
import seedu.FlowCLI.project.ProjectList;

public class ArgumentParser {
    private String argument;
    private ProjectList projects;

    private Project targetProject;
    private String remainingArgument;

    public Project getTargetProject() {
        return targetProject;
    }

    public String getRemainingArgument() {
        return remainingArgument;
    }

    public ArgumentParser(String argument, ProjectList projects) {
        this.argument = argument;
        this.projects = projects;
        parseArgument();

    }

    public void parseArgument() {
        if(argument.isEmpty()) {
            targetProject = null;
            remainingArgument = null;
            return;
        }

        String[] parts = argument.trim().split("\\s+", 2);
        String projectName = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : null;

        if (projects.getProject(projectName) != null) {
            targetProject = projects.getProject(projectName);
            remainingArgument = arguments;
        } else  {
            targetProject = null;
            remainingArgument = argument;
        }

    }
}
