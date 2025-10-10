package seedu.flowcli.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a basic task with a description and completion status.
 * This is the abstract base class for all task types in the application.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected LocalDate deadline;
    protected int priority; // 1=Low, 2=Medium, 3=High

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.deadline = null; // No deadline by default
        this.priority = 2; // Medium priority by default
    }

    public Task(String description, LocalDate deadline, int priority) {
        this.description = description;
        this.isDone = false;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public String getPriorityString() {
        switch (priority) {
        case 1:
            return "Low";
        case 2:
            return "Medium";
        case 3:
            return "High";
        default:
            return "Unknown";
        }
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String marker() {
        return isDone ? "[X]" : "[ ]";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(marker()).append(" ").append(description);

        if (deadline != null) {
            sb.append(" (Due: ").append(deadline.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))).append(")");
        }

        sb.append(" [").append(getPriorityString()).append("]");

        return sb.toString();
    }

    public boolean getDone() {
        return isDone;
    }

}
