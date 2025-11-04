package seedu.flowcli.storage;

import seedu.flowcli.exceptions.DataCorruptedException;
import seedu.flowcli.exceptions.StorageException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Handles loading and saving of FlowCLI data to/from a text file.
 * Implements atomic saves and comprehensive error handling.
 */
//@@author Zhenzha0
public class Storage {
    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE = "flowcli-data.txt";
    private static final String TEMP_FILE = "flowcli-data.tmp";
    private static final String BACKUP_FILE = "flowcli-data.txt.backup";
    
    private static final String PROJECT_PREFIX = "PROJECT|";
    private static final String TASK_PREFIX = "TASK|";
    private static final String DELIMITER = "|";
    private static final String DELIMITER_ESCAPE = "<PIPE>";
    private static final String NEWLINE_ESCAPE = "<NEWLINE>";
    
    private final String dataFilePath;
    private final String tempFilePath;
    private final String backupFilePath;
    private final String dataDirectory;

    /**
     * Creates a Storage instance with default data directory.
     */
    public Storage() {
        this.dataDirectory = DATA_DIRECTORY;
        this.dataFilePath = DATA_DIRECTORY + File.separator + DATA_FILE;
        this.tempFilePath = DATA_DIRECTORY + File.separator + TEMP_FILE;
        this.backupFilePath = DATA_DIRECTORY + File.separator + BACKUP_FILE;
    }

    /**
     * Creates a Storage instance with custom file path (for testing).
     */
    public Storage(String customFilePath) {
        this.dataFilePath = customFilePath;
        this.dataDirectory = new File(customFilePath).getParent();
        this.tempFilePath = dataDirectory + File.separator + TEMP_FILE;
        this.backupFilePath = customFilePath + ".backup";
    }

    /**
     * Loads project data from storage file.
     * 
     * @return ProjectList loaded from file, or empty ProjectList if file doesn't exist
     * @throws StorageException if there's an I/O error during loading
     * @throws DataCorruptedException if the data file is corrupted (file will be backed up)
     */
    public ProjectList load() throws StorageException, DataCorruptedException {
        File file = new File(dataFilePath);
        
        // First run: file doesn't exist
        if (!file.exists()) {
            return new ProjectList();
        }
        
        // File exists but is empty - treat as empty project list
        if (file.length() == 0) {
            return new ProjectList();
        }
        
        try {
            return loadFromFile(file);
        } catch (DataCorruptedException e) {
            // Backup corrupted file
            backupCorruptedFile();
            throw e;
        } catch (IOException e) {
            throw new StorageException("Failed to read data file: " + e.getMessage(), e);
        }
    }

    /**
     * Saves project data to storage file using atomic write.
     * 
     * @param projects The ProjectList to save
     * @throws StorageException if there's an error during saving
     */
    public void save(ProjectList projects) throws StorageException {
        // Ensure data directory exists
        ensureDataDirectoryExists();
        
        File tempFile = new File(tempFilePath);
        
        try {
            // Write to temporary file first (atomic save)
            writeToFile(projects, tempFile);
            
            // Move temp file to actual file (atomic operation)
            Path source = Paths.get(tempFilePath);
            Path target = Paths.get(dataFilePath);
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, 
                      StandardCopyOption.ATOMIC_MOVE);
            
        } catch (AccessDeniedException e) {
            throw new StorageException("Permission denied: Cannot write to " + dataFilePath 
                + ". Please check file permissions.", e);
        } catch (IOException e) {
            // Clean up temp file if it exists
            if (tempFile.exists()) {
                tempFile.delete();
            }
            
            String message = e.getMessage();
            if (message != null && message.toLowerCase().contains("space")) {
                throw new StorageException("Disk full: Not enough space to save data.", e);
            } else if (message != null && message.toLowerCase().contains("lock")) {
                throw new StorageException("File is locked by another process.", e);
            } else {
                throw new StorageException("Failed to save data: " + message, e);
            }
        }
    }

    /**
     * Ensures the data directory exists, creating it if necessary.
     * 
     * @throws StorageException if directory cannot be created
     */
    private void ensureDataDirectoryExists() throws StorageException {
        File dir = new File(dataDirectory);
        
        if (dir.exists() && !dir.isDirectory()) {
            throw new StorageException("Cannot create data directory: A file named 'data' already exists. "
                + "Please remove or rename it.");
        }
        
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new StorageException("Failed to create data directory: " + dataDirectory);
            }
        }
    }

    /**
     * Loads project data from the given file.
     */
    private ProjectList loadFromFile(File file) throws IOException, DataCorruptedException {
        ProjectList projects = new ProjectList();
        Project currentProject = null;
        int lineNumber = 0;
        
        try (BufferedReader reader = new BufferedReader(
                new FileReader(file, StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                if (line.startsWith(PROJECT_PREFIX)) {
                    currentProject = parseProjectLine(line, lineNumber);
                    projects.addProject(currentProject.getProjectName());
                    // Update reference to the added project
                    try {
                        currentProject = projects.getProjectByIndex(projects.getProjectListSize() - 1);
                    } catch (Exception e) {
                        throw new DataCorruptedException("Line " + lineNumber 
                            + ": Failed to add project - " + e.getMessage());
                    }
                    
                } else if (line.startsWith(TASK_PREFIX)) {
                    if (currentProject == null) {
                        throw new DataCorruptedException("Line " + lineNumber 
                            + ": Task found without a project header");
                    }
                    parseAndAddTask(line, currentProject, lineNumber);
                    
                } else {
                    throw new DataCorruptedException("Line " + lineNumber 
                        + ": Invalid line format (expected PROJECT or TASK)");
                }
            }
        }
        
        return projects;
    }

    /**
     * Parses a PROJECT line and creates a Project object.
     */
    private Project parseProjectLine(String line, int lineNumber) throws DataCorruptedException {
        String content = line.substring(PROJECT_PREFIX.length());
        String projectName = unescape(content).trim();
        
        if (projectName.isEmpty()) {
            throw new DataCorruptedException("Line " + lineNumber 
                + ": Project name cannot be empty");
        }
        
        try {
            return new Project(projectName);
        } catch (IllegalArgumentException e) {
            throw new DataCorruptedException("Line " + lineNumber 
                + ": Invalid project name - " + e.getMessage());
        }
    }

    /**
     * Parses a TASK line and adds it to the current project.
     * Format: TASK|isDone|description|deadline|priority
     */
    private void parseAndAddTask(String line, Project project, int lineNumber) 
            throws DataCorruptedException {
        String content = line.substring(TASK_PREFIX.length());
        String[] parts = content.split("\\|", -1); // -1 to keep trailing empty strings
        
        if (parts.length != 4) {
            throw new DataCorruptedException("Line " + lineNumber 
                + ": Invalid task format (expected 4 fields, got " + parts.length + ")");
        }
        
        try {
            // Parse isDone
            int isDoneValue = Integer.parseInt(parts[0].trim());
            if (isDoneValue != 0 && isDoneValue != 1) {
                throw new DataCorruptedException("Line " + lineNumber 
                    + ": Invalid isDone value (must be 0 or 1)");
            }
            boolean isDone = isDoneValue == 1;
            
            // Parse description (can be empty)
            String description = unescape(parts[1]);
            
            // Parse deadline (can be "null")
            LocalDate deadline = null;
            if (!parts[2].trim().equals("null")) {
                try {
                    deadline = LocalDate.parse(parts[2].trim());
                } catch (DateTimeParseException e) {
                    throw new DataCorruptedException("Line " + lineNumber 
                        + ": Invalid date format '" + parts[2] + "' (expected YYYY-MM-DD)");
                }
            }
            
            // Parse priority
            int priority = Integer.parseInt(parts[3].trim());
            if (priority < 1 || priority > 3) {
                throw new DataCorruptedException("Line " + lineNumber 
                    + ": Invalid priority value (must be 1, 2, or 3)");
            }
            
            // Add task to project
            if (deadline != null) {
                project.addTask(description, deadline, priority);
            } else {
                project.addTask(description);
                // Set priority manually since single-arg constructor defaults to 2
                project.getProjectTasks().getTasks()
                    .get(project.getProjectTasks().size() - 1).setPriority(priority);
            }
            
            // Set isDone status
            if (isDone) {
                try {
                    project.getProjectTasks().mark(project.getProjectTasks().size() - 1);
                } catch (Exception e) {
                    throw new DataCorruptedException("Line " + lineNumber 
                        + ": Failed to mark task - " + e.getMessage());
                }
            }
            
        } catch (NumberFormatException e) {
            throw new DataCorruptedException("Line " + lineNumber 
                + ": Invalid number format - " + e.getMessage());
        } catch (DataCorruptedException e) {
            // Re-throw DataCorruptedException as-is
            throw e;
        } catch (Exception e) {
            throw new DataCorruptedException("Line " + lineNumber 
                + ": Error parsing task - " + e.getMessage());
        }
    }

    /**
     * Writes project data to the given file.
     */
    private void writeToFile(ProjectList projects, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(file, StandardCharsets.UTF_8))) {
            
            for (int i = 0; i < projects.getProjectListSize(); i++) {
                Project project;
                try {
                    project = projects.getProjectByIndex(i);
                } catch (Exception e) {
                    // This should never happen since we're iterating within size bounds
                    throw new IOException("Failed to access project at index " + i, e);
                }
                
                // Write project line
                writer.write(PROJECT_PREFIX + escape(project.getProjectName()));
                writer.newLine();
                
                // Write task lines
                for (int j = 0; j < project.getProjectTasks().size(); j++) {
                    seedu.flowcli.task.Task task;
                    try {
                        task = project.getProjectTasks().get(j);
                    } catch (Exception e) {
                        // This should never happen since we're iterating within size bounds
                        throw new IOException("Failed to access task at index " + j, e);
                    }
                    
                    String isDone = task.isDone() ? "1" : "0";
                    String description = escape(task.getDescription());
                    String deadline = task.getDeadline() == null ? "null" : task.getDeadline().toString();
                    String priority = String.valueOf(task.getPriority());
                    
                    writer.write(TASK_PREFIX + isDone + DELIMITER + description + DELIMITER 
                               + deadline + DELIMITER + priority);
                    writer.newLine();
                }
            }
        }
    }

    /**
     * Backs up a corrupted data file.
     */
    private void backupCorruptedFile() {
        try {
            File source = new File(dataFilePath);
            File backup = new File(backupFilePath);
            
            if (source.exists()) {
                Files.copy(source.toPath(), backup.toPath(), 
                          StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            // Backup failed, but we'll still proceed with empty data
            // This is logged but not thrown to avoid blocking the user
            System.err.println("Warning: Could not backup corrupted file: " + e.getMessage());
        }
    }

    /**
     * Escapes special characters in strings for storage.
     * Escapes: | (delimiter) and newlines
     */
    private String escape(String input) {
        if (input == null) {
            return "";
        }
        // Order matters: escape the placeholder first to avoid double-escaping
        return input.replace("<PIPE>", "<PIPE><PIPE>")  // Escape our escape sequence
                    .replace("<NEWLINE>", "<NEWLINE><NEWLINE>")
                    .replace("|", DELIMITER_ESCAPE)
                    .replace("\n", NEWLINE_ESCAPE)
                    .replace("\r", ""); // Remove carriage returns
    }

    /**
     * Unescapes special characters from stored strings.
     */
    private String unescape(String input) {
        if (input == null) {
            return "";
        }
        // Order matters: unescape in reverse order
        return input.replace(NEWLINE_ESCAPE, "\n")
                    .replace(DELIMITER_ESCAPE, "|")
                    .replace("<NEWLINE><NEWLINE>", "<NEWLINE>")
                    .replace("<PIPE><PIPE>", "<PIPE>");
    }

    /**
     * Gets the data file path.
     */
    public String getDataFilePath() {
        return dataFilePath;
    }
}
//@@author

