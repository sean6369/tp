package seedu.flowcli.commands.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.flowcli.commands.validation.ValidationConstants;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.task.TaskWithProject;

/**
 * Sorts tasks globally across all projects based on deadline or priority.
 */
public class TaskSorter {
    private static final Logger logger = Logger.getLogger(TaskSorter.class.getName());

    private final ProjectList projects;
    private final List<TaskWithProject> inputTasks;
    private final String sortBy;
    private final boolean ascending;
    private List<TaskWithProject> sortedTasks;

    /**
     * Constructor for sorting tasks from all projects.
     */
    public TaskSorter(ProjectList projects, String sortBy, boolean ascending) {
        assert projects != null : "ProjectList cannot be null";
        assert sortBy != null && (sortBy.equals("deadline")
                                        || sortBy.equals("priority")) : "sortBy must be 'deadline' or 'priority'";

        this.projects = projects;
        this.inputTasks = null;
        this.sortBy = sortBy;
        this.ascending = ascending;

        logger.info(String.format("Creating TaskSorter with sortBy='%s', ascending=%b", sortBy, ascending));

        sort();
    }

    /**
     * Constructor for sorting a specific list of tasks.
     */
    public TaskSorter(List<TaskWithProject> tasks, String sortBy, boolean ascending) {
        assert tasks != null : "Task list cannot be null";
        assert sortBy != null && (sortBy.equals("deadline")
                                        || sortBy.equals("priority")) : "sortBy must be 'deadline' or 'priority'";

        this.projects = null;
        this.inputTasks = tasks;
        this.sortBy = sortBy;
        this.ascending = ascending;

        logger.info(String.format("Creating TaskSorter with sortBy='%s', ascending=%b on %d tasks", sortBy, ascending,
                                        tasks.size()));

        sort();
    }

    public List<TaskWithProject> getSortedTasks() {
        return sortedTasks;
    }

    private void sort() {
        logger.fine("Starting task sorting process");

        if (inputTasks != null) {
            sortedTasks = new ArrayList<>(inputTasks);
            logger.fine(String.format("Sorting %d provided tasks by %s", sortedTasks.size(), sortBy));
        } else {
            sortedTasks = TaskCollector.getAllTasksWithProjects(projects);
            logger.fine(String.format("Collected %d tasks for sorting by %s", sortedTasks.size(), sortBy));
        }

        long startTime = System.nanoTime();

        sortedTasks.sort((t1, t2) -> {
            Task task1 = t1.getTask();
            Task task2 = t2.getTask();
            int comparison = 0;

            if (ValidationConstants.SORT_FIELD_DEADLINE.equals(sortBy)) {
                if (task1.getDeadline() == null && task2.getDeadline() == null) {
                    comparison = 0;
                } else if (task1.getDeadline() == null) {
                    comparison = 1;
                } else if (task2.getDeadline() == null) {
                    comparison = -1;
                } else {
                    comparison = task1.getDeadline().compareTo(task2.getDeadline());
                }
            } else if (ValidationConstants.SORT_FIELD_PRIORITY.equals(sortBy)) {
                comparison = Integer.compare(task1.getPriority(), task2.getPriority());
            }

            return ascending ? comparison : -comparison;
        });

        long duration = System.nanoTime() - startTime;
        logger.info(String.format("Task sorting completed in %d ns. Sorted %d tasks by %s (%s)", duration,
                                        sortedTasks.size(), sortBy, ascending ? "ascending" : "descending"));
    }
}
