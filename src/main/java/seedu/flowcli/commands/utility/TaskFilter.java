package seedu.flowcli.commands.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;

/**
 * Filters tasks globally across all projects based on priority and/or project
 * name.
 */
public class TaskFilter {
    private static final Logger logger = Logger.getLogger(TaskFilter.class.getName());

    private final ProjectList projects;
    private final List<TaskWithProject> inputTasks;
    private final String priorityFilter;
    private final String projectNameFilter;
    private List<TaskWithProject> filteredTasks;

    /**
     * Constructor for filtering tasks from all projects.
     */
    public TaskFilter(ProjectList projects, String priority, String projectName) {
        assert projects != null : "Project list cannot be null";

        this.projects = projects;
        this.inputTasks = null;
        this.priorityFilter = priority;
        this.projectNameFilter = projectName;

        logger.fine(String.format("Creating TaskFilter with priority='%s', project='%s'", priorityFilter,
                projectNameFilter));

        filter();
    }

    /**
     * Constructor for filtering a specific list of tasks.
     */
    public TaskFilter(List<TaskWithProject> tasks, String priority, String projectName) {
        assert tasks != null : "Task list cannot be null";

        this.projects = null;
        this.inputTasks = tasks;
        this.priorityFilter = priority;
        this.projectNameFilter = projectName;

        logger.fine(String.format("Creating TaskFilter with priority='%s', project='%s' on %d tasks", priorityFilter,
                projectNameFilter, tasks.size()));

        filter();
    }

    public List<TaskWithProject> getFilteredTasks() {
        return filteredTasks;
    }

    private void filter() {
        logger.fine("Starting task filtering process");

        filteredTasks = new ArrayList<>();
        int totalTasksProcessed = 0;

        if (inputTasks != null) {
            for (TaskWithProject taskWithProject : inputTasks) {
                totalTasksProcessed++;

                Task task = taskWithProject.getTask();
                String projectName = taskWithProject.getProjectName();

                if (projectNameFilter != null && !projectName.equalsIgnoreCase(projectNameFilter)) {
                    continue;
                }

                if (priorityFilter != null) {
                    String taskPriority = task.getPriorityString().toLowerCase();
                    if (!taskPriority.equals(priorityFilter.toLowerCase())) {
                        continue;
                    }
                }

                filteredTasks.add(taskWithProject);
                logger.fine(String.format("Added task '%s' from project '%s' to filtered results",
                        task.getDescription(), projectName));
            }
        } else {
            for (Project project : projects.getProjectList()) {
                if (projectNameFilter != null && !project.getProjectName().equalsIgnoreCase(projectNameFilter)) {
                    continue;
                }

                for (Task task : project.getProjectTasks().getTasks()) {
                    totalTasksProcessed++;

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
        }

        logger.fine(String.format("Task filtering completed. Processed %d tasks, found %d matches", totalTasksProcessed,
                filteredTasks.size()));
    }
}
