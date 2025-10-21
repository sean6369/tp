package seedu.flowcli.commands.utility;

import java.util.ArrayList;
import java.util.List;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;

/**
 * Utility class for collecting tasks from projects.
 */
public final class TaskCollector {
    private TaskCollector() {
    }

    /**
     * Collects all tasks from all projects with their project names.
     *
     * @param projects The project list to collect tasks from
     * @return List of tasks with their associated project names
     */
    public static List<TaskWithProject> getAllTasksWithProjects(ProjectList projects) {
        List<TaskWithProject> tasks = new ArrayList<>();
        for (Project project : projects.getProjectList()) {
            tasks.addAll(getTasksFromProject(project));
        }
        return tasks;
    }

    /**
     * Collects all tasks from a specific project with the project name.
     *
     * @param project The project to collect tasks from
     * @return List of tasks with their associated project name
     */
    public static List<TaskWithProject> getTasksFromProject(Project project) {
        List<TaskWithProject> tasks = new ArrayList<>();
        for (Task task : project.getProjectTasks().getTasks()) {
            tasks.add(new TaskWithProject(project.getProjectName(), task));
        }
        return tasks;
    }
}
