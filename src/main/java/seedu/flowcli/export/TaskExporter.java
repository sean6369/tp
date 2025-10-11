package seedu.flowcli.export;

import seedu.flowcli.exception.FlowCLIExceptions.FileWriteException;
import seedu.flowcli.task.TaskWithProject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for exporting tasks to TXT files.
 * Handles file I/O operations and formatting for task exports.
 */
public class TaskExporter {
    /**
     * Exports a list of tasks to a TXT file with a header.
     *
     * @param tasks List of tasks with project information to export
     * @param filename The name of the file to write to
     * @param header Optional header text to include at the top of the file
     * @throws FileWriteException if there's an error writing to the file
     */
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
