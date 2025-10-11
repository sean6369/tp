package seedu.flowcli.tools;

import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;

import java.util.List;
import java.util.logging.Logger;
import seedu.flowcli.validation.ValidationConstants;

/**
 * Sorts tasks globally across all projects based on deadline or priority.
 */
public class TaskSorter {
    private static final Logger logger = Logger.getLogger(TaskSorter.class.getName());

    private ProjectList projects;
    private String sortBy; // "deadline" or "priority"
    private boolean ascending; // true for ascending, false for descending
    private List<TaskWithProject> sortedTasks;

    public TaskSorter(ProjectList projects, String sortBy, boolean ascending) {
        // Validate parameters
        assert projects != null : "ProjectList cannot be null";
        assert sortBy != null && (sortBy.equals("deadline") || sortBy.equals("priority")) :
            "sortBy must be 'deadline' or 'priority'";

        this.projects = projects;
        this.sortBy = sortBy;
        this.ascending = ascending;

        logger.info(String.format("Creating TaskSorter with sortBy='%s', ascending=%b", sortBy, ascending));

        sort();
    }

    public List<TaskWithProject> getSortedTasks() {
        return sortedTasks;
    }

    private void sort() {
        logger.fine("Starting task sorting process");

        sortedTasks = TaskCollector.getAllTasksWithProjects(projects);
        logger.fine(String.format("Collected %d tasks for sorting by %s", sortedTasks.size(), sortBy));

        // Sort the tasks
        long startTime = System.nanoTime();

        sortedTasks.sort((t1, t2) -> {
            Task task1 = t1.getTask();
            Task task2 = t2.getTask();
            int comparison = 0;

            if (ValidationConstants.SORT_FIELD_DEADLINE.equals(sortBy)) {
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
            } else if (ValidationConstants.SORT_FIELD_PRIORITY.equals(sortBy)) {
                comparison = Integer.compare(task1.getPriority(), task2.getPriority());
            }

            return ascending ? comparison : -comparison;
        });

        long duration = System.nanoTime() - startTime;
        logger.info(String.format("Task sorting completed in %d ns. Sorted %d tasks by %s (%s)",
            duration, sortedTasks.size(), sortBy, ascending ? "ascending" : "descending"));
    }
}
