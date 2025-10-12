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
    private List<TaskWithProject> inputTasks; // Optional list of tasks to filter
    private String priorityFilter; // "high", "medium", "low", or null
    private String projectNameFilter; // project name or null
    private List<TaskWithProject> filteredTasks;

    /**
     * Constructor for filtering tasks from all projects.
     */
    public TaskFilter(ProjectList projects, String priority, String projectName) {
        // Validate parameters
        assert projects != null : "ProjectList cannot be null";

        this.projects = projects;
        this.inputTasks = null; // Will fetch from projects
        this.priorityFilter = priority;
        this.projectNameFilter = projectName;

        logger.info(String.format("Creating TaskFilter with priority='%s', project='%s'",
                priorityFilter, projectNameFilter));

        filter();
    }

    /**
     * Constructor for filtering a specific list of tasks.
     */
    public TaskFilter(List<TaskWithProject> tasks, String priority, String projectName) {
        // Validate parameters
        assert tasks != null : "Task list cannot be null";

        this.projects = null; // Not needed when filtering specific tasks
        this.inputTasks = tasks;
        this.priorityFilter = priority;
        this.projectNameFilter = projectName;

        logger.info(String.format("Creating TaskFilter with priority='%s', project='%s' on %d tasks",
                priorityFilter, projectNameFilter, tasks.size()));

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
            // Filter the provided list of tasks
            for (TaskWithProject taskWithProject : inputTasks) {
                totalTasksProcessed++;
                
                Task task = taskWithProject.getTask();
                String projectName = taskWithProject.getProjectName();

                // Check project name filter
                if (projectNameFilter != null) {
                    if (!projectName.equalsIgnoreCase(projectNameFilter)) {
                        continue;
                    }
                }

                // Check priority filter
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
            // Filter tasks from all projects (original behavior)
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
        }

        logger.info(String.format("Task filtering completed. Processed %d tasks, found %d matches",
                totalTasksProcessed, filteredTasks.size()));
    }
}
