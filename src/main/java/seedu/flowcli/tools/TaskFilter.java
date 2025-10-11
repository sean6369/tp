package seedu.flowcli.tools;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Filters tasks globally across all projects based on priority and/or project
 * name.
 */
public class TaskFilter {
    private static final Logger logger = Logger.getLogger(TaskFilter.class.getName());

    private ProjectList projects;
    private String priorityFilter; // "high", "medium", "low", or null
    private String projectNameFilter; // project name or null
    private List<TaskWithProject> filteredTasks;

    public TaskFilter(ProjectList projects, String priority, String projectName) {
        // Validate parameters
        assert projects != null : "ProjectList cannot be null";

        this.projects = projects;
        this.priorityFilter = priority;
        this.projectNameFilter = projectName;

        logger.info(String.format("Creating TaskFilter with priority='%s', project='%s'",
                priorityFilter, projectNameFilter));

        filter();
    }

    public List<TaskWithProject> getFilteredTasks() {
        return filteredTasks;
    }

    private void filter() {
        logger.fine("Starting task filtering process");

        filteredTasks = new ArrayList<>();
        int totalTasksProcessed = 0;

        for (Project project : projects.getProjectList()) {
            // Check project name filter
            if (projectNameFilter != null) {
                if (!project.getProjectName().equalsIgnoreCase(projectNameFilter)) {
                    continue;
                }
            }

            for (Task task : project.getProjectTasks().getTasks()) {
                totalTasksProcessed++;

                // Check priority filter
                if (priorityFilter != null) {
                    String taskPriority = task.getPriorityString().toLowerCase();
                    if (!taskPriority.equals(priorityFilter.toLowerCase())) {
                        continue;
                    }
                }

                filteredTasks.add(new TaskWithProject(project.getProjectName(), task));
                logger.fine(String.format("Added task '%s' from project '%s' to filtered results",
                        task.getDescription(), project.getProjectName()));
            }
        }

        logger.info(String.format("Task filtering completed. Processed %d tasks, found %d matches",
                totalTasksProcessed, filteredTasks.size()));
    }
}
