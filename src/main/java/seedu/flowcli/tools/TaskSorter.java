package seedu.flowcli.tools;

import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorts tasks globally across all projects based on deadline or priority.
 */
public class TaskSorter {
    private ProjectList projects;
    private String sortBy; // "deadline" or "priority"
    private boolean ascending; // true for ascending, false for descending
    private List<SortedTask> sortedTasks;

    /**
     * Represents a task with its associated project information for sorting.
     */
    public static class SortedTask {
        private final String projectName;
        private final Task task;

        public SortedTask(String projectName, Task task) {
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

    public TaskSorter(ProjectList projects, String sortBy, boolean ascending) {
        this.projects = projects;
        this.sortBy = sortBy;
        this.ascending = ascending;
        sort();
    }

    public List<SortedTask> getSortedTasks() {
        return sortedTasks;
    }

    private void sort() {
        sortedTasks = new ArrayList<>();
        for (Project project : projects.getProjectList()) {
            for (Task task : project.getProjectTasks().getTasks()) {
                sortedTasks.add(new SortedTask(project.getProjectName(), task));
            }
        }

        sortedTasks.sort((t1, t2) -> {
            Task task1 = t1.getTask();
            Task task2 = t2.getTask();
            int comparison = 0;

            if ("deadline".equals(sortBy)) {
                // Handle null deadlines: nulls last in ascending
                if (task1.getDeadline() == null && task2.getDeadline() == null) {
                    comparison = 0;
                } else if (task1.getDeadline() == null) {
                    comparison = 1; // task1 after task2
                } else if (task2.getDeadline() == null) {
                    comparison = -1; // task1 before task2
                } else {
                    comparison = task1.getDeadline().compareTo(task2.getDeadline());
                }
            } else if ("priority".equals(sortBy)) {
                comparison = Integer.compare(task1.getPriority(), task2.getPriority());
            }

            return ascending ? comparison : -comparison;
        });
    }
}
