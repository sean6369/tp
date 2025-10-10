package seedu.flowcli.tools;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Filters tasks globally across all projects based on priority and/or project name.
 */
public class TaskFilter {
    private ProjectList projects;
    private String priorityFilter; // "high", "medium", "low", or null
    private String projectNameFilter; // project name or null
    private List<FilteredTask> filteredTasks;

    /**
     * Represents a task with its associated project information.
     */
    public static class FilteredTask {
        private final String projectName;
        private final Task task;

        public FilteredTask(String projectName, Task task) {
            this.projectName = projectName;
            this.task = task;
        }

        public String getProjectName() {
            return projectName;
        }

        public Task getTask() {
            return task;
        }

        @Override
        public String toString() {
            return projectName + ": " + task.toString();
        }
    }

    public TaskFilter(ProjectList projects, String priority, String projectName) {
        this.projects = projects;
        this.priorityFilter = priority;
        this.projectNameFilter = projectName;
        filter();
    }

    public List<FilteredTask> getFilteredTasks() {
        return filteredTasks;
    }

    private void filter() {
        filteredTasks = new ArrayList<>();
        for (Project project : projects.getProjectList()) {
            // Check project name filter
            if (projectNameFilter != null) {
                if (!project.getProjectName().equalsIgnoreCase(projectNameFilter)) {
                    continue;
                }
            }

            for (Task task : project.getProjectTasks().getTasks()) {
                // Check priority filter
                if (priorityFilter != null) {
                    String taskPriority = task.getPriorityString().toLowerCase();
                    if (!taskPriority.equals(priorityFilter.toLowerCase())) {
                        continue;
                    }
                }

                filteredTasks.add(new FilteredTask(project.getProjectName(), task));
            }
        }
    }
}
