package seedu.flowcli.export;

import seedu.flowcli.exception.FlowCLIExceptions.FileWriteException;
import seedu.flowcli.task.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for exporting tasks to TXT files.
 * Handles file I/O operations and formatting for task exports.
 */
public class TaskExporter {

    /**
     * Represents a task with its associated project information for export.
     */
    public static class ExportableTask {
        private final String projectName;
        private final Task task;

        public ExportableTask(String projectName, Task task) {
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

    /**
     * Exports a list of tasks to a TXT file with a header.
     *
     * @param tasks List of tasks with project information to export
     * @param filename The name of the file to write to
     * @param header Optional header text to include at the top of the file
     * @throws FileWriteException if there's an error writing to the file
     */
    public static void exportTasksToFile(List<ExportableTask> tasks, String filename, String header)
            throws FileWriteException {
        try (FileWriter writer = new FileWriter(filename)) {
            if (header != null && !header.isEmpty()) {
                writer.write(header + "\n");
                writer.write("=".repeat(header.length()) + "\n\n");
            }

            for (ExportableTask exportableTask : tasks) {
                writer.write(exportableTask.toString() + "\n");
            }
        } catch (IOException e) {
            throw new FileWriteException(e.getMessage());
        }
    }
}
