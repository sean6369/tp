package seedu.flowcli.commands.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
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
        } catch (AccessDeniedException e) {
            // Permission denied - user doesn't have write access
            throw new FileWriteException(
                    "'" + filename + "': Permission denied. " +
                            "Check that you have write permissions for this location.");
        } catch (NoSuchFileException e) {
            // Parent directory doesn't exist
            throw new FileWriteException(
                    "'" + filename + "': Directory does not exist. " +
                            "Check that the path is correct.");
        } catch (FileSystemException e) {
            // Various file system errors (disk full, read-only filesystem, etc.)
            String message = e.getMessage();
            if (message != null && message.toLowerCase().contains("space")) {
                throw new FileWriteException(
                        "'" + filename + "': Not enough disk space.");
            } else if (message != null && message.toLowerCase().contains("read-only")) {
                throw new FileWriteException(
                        "'" + filename + "': Location is read-only.");
            } else {
                throw new FileWriteException(
                        "'" + filename + "': " +
                                (message != null ? message : "File system error."));
            }
        } catch (SecurityException e) {
            // Security manager denied access
            throw new FileWriteException(
                    "'" + filename + "': Access denied by security policy.");
        } catch (IOException e) {
            // Generic I/O error - check for common cases
            String message = e.getMessage();

            // Check if file is locked/in use
            if (message != null && (message.contains("being used") || message.contains("locked"))) {
                throw new FileWriteException(
                        "'" + filename + "': File is currently open or locked " +
                                "by another program.");
            }

            // Check if path is too long
            if (message != null && message.contains("too long")) {
                throw new FileWriteException(
                        "'" + filename + "': File path is too long.");
            }

            // Check for directory not found
            if (message != null && (message.contains("No such file") || 
                    message.contains("cannot find the path"))) {
                throw new FileWriteException(
                        "'" + filename + "': Directory does not exist. " +
                                "Check that the path is correct.");
            }
            
            // Check for permission denied
            if (message != null && message.contains("Permission denied")) {
                throw new FileWriteException(
                        "'" + filename + "': Permission denied. " +
                                "Check that you have write permissions for this location.");
            }

            // Generic error
            throw new FileWriteException(
                    "'" + filename + "': " +
                            (message != null ? message : "Unknown I/O error."));
        }
    }

}
