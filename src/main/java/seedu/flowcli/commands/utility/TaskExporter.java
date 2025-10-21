package seedu.flowcli.commands.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import seedu.flowcli.exceptions.FileWriteException;
import seedu.flowcli.task.TaskWithProject;

/**
 * Utility class for exporting tasks to TXT files. Handles file I/O operations
 * and formatting.
 */
public final class TaskExporter {
    private TaskExporter() {
    }

    public static void exportTasksToFile(List<TaskWithProject> tasks, String filename, String header)
                                    throws FileWriteException {
        try (FileWriter writer = new FileWriter(filename)) {
            if (header != null && !header.isEmpty()) {
                writer.write(header + "\n");
                writer.write("=".repeat(header.length()) + "\n\n");
            }

            for (TaskWithProject taskWithProject : tasks) {
                writer.write(taskWithProject.toString() + "\n");
            }
        } catch (IOException e) {
            throw new FileWriteException(e.getMessage());
        }
    }
}
