package seedu.flowcli.task;

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


    public int size() { return tasks.size(); }

    public boolean isEmpty() { return tasks.isEmpty(); }

    public Task get(int zeroBasedIndex) {
        return tasks.get(zeroBasedIndex);
    }

    public void mark(int zeroBasedIndex) {
        tasks.get(zeroBasedIndex).mark();
    }

    public void unmark(int zeroBasedIndex) {
        tasks.get(zeroBasedIndex).unmark();
    }

    public Task delete(int zeroBasedIndex) {
        Task returnTask = tasks.get(zeroBasedIndex);
        tasks.remove(zeroBasedIndex);
        return returnTask;

    }

    public List<Task> getTasks() { return tasks; }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append('\n');
        }
        return sb.toString();
    }
}


