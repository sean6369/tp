package task;

/**
 * Represents a basic task with a description and completion status.
 * This is the abstract base class for all task types in the application.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String marker() { return isDone ? "[X]" : "[ ]"; }

    public String toString() {
        return marker() + " " + description;
    }

    public boolean getDone() {
        return isDone;
    }

}

