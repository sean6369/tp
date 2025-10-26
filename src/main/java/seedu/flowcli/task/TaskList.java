package seedu.flowcli.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of tasks.
 * Provides methods to add, remove, mark, and retrieve tasks.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(String description) {
        tasks.add(new Task(description));
    }

    public void addTask(String description, LocalDate deadline, int priority) {
        tasks.add(new Task(description, deadline, priority));
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    private void validateIndex(int zeroBasedIndex) {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= tasks.size()) {
            throw new IndexOutOfBoundsException("Task index " + (zeroBasedIndex + 1) +
                    " out of range. Valid range: 1-" + tasks.size());
        }
    }

    public Task get(int zeroBasedIndex) {
        validateIndex(zeroBasedIndex);
        return tasks.get(zeroBasedIndex);
    }

    public void mark(int zeroBasedIndex) {
        validateIndex(zeroBasedIndex);
        tasks.get(zeroBasedIndex).mark();
    }

    public void unmark(int zeroBasedIndex) {
        validateIndex(zeroBasedIndex);
        tasks.get(zeroBasedIndex).unmark();
    }

    public Task delete(int zeroBasedIndex) {
        validateIndex(zeroBasedIndex);
        Task returnTask = tasks.get(zeroBasedIndex);
        tasks.remove(zeroBasedIndex);
        return returnTask;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void sortByDeadline(boolean ascending) {
        tasks.sort((t1, t2) -> {
            // Handle null deadlines
            if (t1.getDeadline() == null && t2.getDeadline() == null) {
                return 0; // Both have no deadline, maintain relative order
            }
            if (t1.getDeadline() == null) {
                return ascending ? 1 : -1; // Null deadlines go to end for ascending, beginning for descending
            }
            if (t2.getDeadline() == null) {
                return ascending ? -1 : 1; // Null deadlines go to end for ascending, beginning for descending
            }

            // Both have deadlines, compare them
            int comparison = t1.getDeadline().compareTo(t2.getDeadline());
            return ascending ? comparison : -comparison;
        });
    }

    public void sortByPriority(boolean ascending) {
        tasks.sort((t1, t2) -> {
            int comparison = Integer.compare(t1.getPriority(), t2.getPriority());
            return ascending ? comparison : -comparison;
        });
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append('\n');
        }
        return sb.toString();
    }
}
