package seedu.flowcli.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.flowcli.exceptions.DataCorruptedException;
import seedu.flowcli.exceptions.StorageException;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {
    @TempDir
    Path tempDir;
    
    private Storage storage;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("test-data.txt").toString();
        storage = new Storage(testFilePath);
    }

    @AfterEach
    void tearDown() {
        // Clean up any created files
        new File(testFilePath).delete();
        new File(testFilePath + ".backup").delete();
        new File(tempDir.resolve("test-data.tmp").toString()).delete();
    }

    // ========== SAVE TESTS ==========

    @Test
    void save_emptyProjectList_success() throws StorageException, DataCorruptedException {
        ProjectList projects = new ProjectList();
        storage.save(projects);
        
        // Verify file was created
        File file = new File(testFilePath);
        assertTrue(file.exists());
        
        // Verify can be loaded back
        ProjectList loaded = storage.load();
        assertEquals(0, loaded.getProjectListSize());
    }

    @Test
    void save_singleProjectNoTasks_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Empty Project");
        
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals(1, loaded.getProjectListSize());
        assertEquals("Empty Project", loaded.getProjectByIndex(0).getProjectName());
        assertEquals(0, loaded.getProjectByIndex(0).size());
    }

    @Test
    void save_projectWithTasks_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("CS2113");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Complete assignment", LocalDate.of(2025, 12, 31), 3);
        project.addTask("Study for exam");
        project.getProjectTasks().mark(0); // Mark first task as done
        
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals(1, loaded.getProjectListSize());
        Project loadedProject = loaded.getProjectByIndex(0);
        assertEquals("CS2113", loadedProject.getProjectName());
        assertEquals(2, loadedProject.size());
        
        // Check first task
        assertEquals("Complete assignment", loadedProject.getProjectTasks().get(0).getDescription());
        assertEquals(LocalDate.of(2025, 12, 31), loadedProject.getProjectTasks().get(0).getDeadline());
        assertEquals(3, loadedProject.getProjectTasks().get(0).getPriority());
        assertTrue(loadedProject.getProjectTasks().get(0).isDone());
        
        // Check second task
        assertEquals("Study for exam", loadedProject.getProjectTasks().get(1).getDescription());
        assertNull(loadedProject.getProjectTasks().get(1).getDeadline());
        assertFalse(loadedProject.getProjectTasks().get(1).isDone());
    }

    @Test
    void save_multipleProjects_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Project A");
        projects.addProject("Project B");
        projects.addProject("Project C");
        
        projects.getProjectByIndex(0).addTask("Task A1");
        projects.getProjectByIndex(1).addTask("Task B1");
        projects.getProjectByIndex(1).addTask("Task B2");
        
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals(3, loaded.getProjectListSize());
        assertEquals("Project A", loaded.getProjectByIndex(0).getProjectName());
        assertEquals("Project B", loaded.getProjectByIndex(1).getProjectName());
        assertEquals("Project C", loaded.getProjectByIndex(2).getProjectName());
        assertEquals(1, loaded.getProjectByIndex(0).size());
        assertEquals(2, loaded.getProjectByIndex(1).size());
        assertEquals(0, loaded.getProjectByIndex(2).size());
    }

    @Test
    void save_taskWithSpecialCharacters_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Test | Project");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Task with | pipe character");
        
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals("Test | Project", loaded.getProjectByIndex(0).getProjectName());
        assertEquals("Task with | pipe character", 
            loaded.getProjectByIndex(0).getProjectTasks().get(0).getDescription());
    }

    @Test
    void save_taskWithEmptyDescription_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Test Project");
        Project project = projects.getProjectByIndex(0);
        project.addTask("");
        
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals("", loaded.getProjectByIndex(0).getProjectTasks().get(0).getDescription());
    }

    @Test
    void save_largeDataset_success() throws Exception {
        ProjectList projects = new ProjectList();
        
        // Create 100 projects with 10 tasks each
        for (int i = 0; i < 100; i++) {
            projects.addProject("Project " + i);
            Project project = projects.getProjectByIndex(i);
            for (int j = 0; j < 10; j++) {
                project.addTask("Task " + j, LocalDate.of(2025, 1, 1).plusDays(j), (j % 3) + 1);
            }
        }
        
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals(100, loaded.getProjectListSize());
        assertEquals(10, loaded.getProjectByIndex(0).size());
        assertEquals(10, loaded.getProjectByIndex(99).size());
    }

    // ========== LOAD TESTS ==========

    @Test
    void load_fileNotExists_returnsEmptyList() throws Exception {
        ProjectList loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(0, loaded.getProjectListSize());
    }

    @Test
    void load_emptyFile_returnsEmptyList() throws Exception {
        // Create empty file
        new File(testFilePath).createNewFile();
        
        ProjectList loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(0, loaded.getProjectListSize());
    }

    @Test
    void load_corruptedFormat_throwsException() throws IOException {
        // Write invalid format
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("INVALID|LINE\n");
        }
        
        assertThrows(DataCorruptedException.class, () -> storage.load());
        
        // Verify backup was created
        File backup = new File(testFilePath + ".backup");
        assertTrue(backup.exists());
    }

    @Test
    void load_taskWithoutProject_throwsException() throws IOException {
        // Write task before project
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("TASK|0|Some task|null|2\n");
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Task found without a project header"));
    }

    @Test
    void load_invalidIsDone_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|2|Some task|null|2\n"); // isDone must be 0 or 1
        }
        
        assertThrows(DataCorruptedException.class, () -> storage.load());
    }

    @Test
    void load_invalidDateFormat_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Some task|invalid-date|2\n");
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    void load_invalidPriority_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Some task|null|5\n"); // Priority must be 1-3
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Invalid priority"));
    }

    @Test
    void load_emptyProjectName_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|\n");
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Project name cannot be empty"));
    }

    @Test
    void load_missingTaskFields_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Some task\n"); // Missing deadline and priority fields
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Invalid task format"));
    }

    @Test
    void load_extraTaskFields_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Some task|null|2|extra\n"); // Too many fields
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Invalid task format"));
    }

    @Test
    void load_skipEmptyLines_success() throws Exception {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("\n"); // Empty line
            writer.write("TASK|0|Task 1|null|2\n");
            writer.write("\n"); // Empty line
            writer.write("TASK|1|Task 2|2025-12-31|3\n");
        }
        
        ProjectList loaded = storage.load();
        assertEquals(1, loaded.getProjectListSize());
        assertEquals(2, loaded.getProjectByIndex(0).size());
    }

    @Test
    void load_escapedDelimiters_success() throws Exception {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test <PIPE> Project\n");
            writer.write("TASK|0|Task <PIPE> with pipe|null|2\n");
        }
        
        ProjectList loaded = storage.load();
        assertEquals("Test | Project", loaded.getProjectByIndex(0).getProjectName());
        assertEquals("Task | with pipe", 
            loaded.getProjectByIndex(0).getProjectTasks().get(0).getDescription());
    }

    @Test
    void load_extremeDates_success() throws Exception {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Old task|1900-01-01|1\n");
            writer.write("TASK|0|Future task|2100-12-31|3\n");
        }
        
        ProjectList loaded = storage.load();
        assertEquals(LocalDate.of(1900, 1, 1), 
            loaded.getProjectByIndex(0).getProjectTasks().get(0).getDeadline());
        assertEquals(LocalDate.of(2100, 12, 31), 
            loaded.getProjectByIndex(0).getProjectTasks().get(1).getDeadline());
    }

    // ========== SAVE-LOAD CYCLE TESTS ==========

    @Test
    void saveThenLoad_preservesAllData() throws Exception {
        ProjectList original = new ProjectList();
        original.addProject("Project 1");
        original.addProject("Project 2");
        
        Project p1 = original.getProjectByIndex(0);
        p1.addTask("Task 1", LocalDate.of(2025, 12, 25), 3);
        p1.addTask("Task 2");
        p1.getProjectTasks().mark(0);
        
        Project p2 = original.getProjectByIndex(1);
        p2.addTask("Task 3", LocalDate.of(2026, 1, 1), 1);
        
        storage.save(original);
        ProjectList loaded = storage.load();
        
        // Verify project count
        assertEquals(2, loaded.getProjectListSize());
        
        // Verify Project 1
        Project lp1 = loaded.getProjectByIndex(0);
        assertEquals("Project 1", lp1.getProjectName());
        assertEquals(2, lp1.size());
        assertTrue(lp1.getProjectTasks().get(0).isDone());
        assertEquals("Task 1", lp1.getProjectTasks().get(0).getDescription());
        assertEquals(LocalDate.of(2025, 12, 25), lp1.getProjectTasks().get(0).getDeadline());
        assertEquals(3, lp1.getProjectTasks().get(0).getPriority());
        
        // Verify Project 2
        Project lp2 = loaded.getProjectByIndex(1);
        assertEquals("Project 2", lp2.getProjectName());
        assertEquals(1, lp2.size());
        assertEquals("Task 3", lp2.getProjectTasks().get(0).getDescription());
        assertEquals(LocalDate.of(2026, 1, 1), lp2.getProjectTasks().get(0).getDeadline());
        assertEquals(1, lp2.getProjectTasks().get(0).getPriority());
    }

    @Test
    void multipleSaveCycles_lastWriteWins() throws Exception {
        // First save
        ProjectList projects1 = new ProjectList();
        projects1.addProject("Project A");
        storage.save(projects1);
        
        // Second save (overwrites)
        ProjectList projects2 = new ProjectList();
        projects2.addProject("Project B");
        storage.save(projects2);
        
        // Load should get the second save
        ProjectList loaded = storage.load();
        assertEquals(1, loaded.getProjectListSize());
        assertEquals("Project B", loaded.getProjectByIndex(0).getProjectName());
    }

    // ========== DATA DIRECTORY TESTS ==========

    @Test
    void save_createsDataDirectoryIfNotExists() throws Exception {
        Path newDir = tempDir.resolve("newdir");
        String newFilePath = newDir.resolve("data.txt").toString();
        Storage newStorage = new Storage(newFilePath);
        
        assertFalse(newDir.toFile().exists());
        
        ProjectList projects = new ProjectList();
        projects.addProject("Test");
        newStorage.save(projects);
        
        assertTrue(newDir.toFile().exists());
        assertTrue(newDir.toFile().isDirectory());
        assertTrue(new File(newFilePath).exists());
    }

    @Test
    void save_dataExistsAsFile_throwsException() throws Exception {
        // Create a file named "data" in temp directory
        Path dataFile = tempDir.resolve("data");
        Files.createFile(dataFile);
        
        String newFilePath = tempDir.resolve("data").resolve("test.txt").toString();
        Storage newStorage = new Storage(newFilePath);
        
        ProjectList projects = new ProjectList();
        StorageException exception = assertThrows(
            StorageException.class, 
            () -> newStorage.save(projects)
        );
        assertTrue(exception.getMessage().contains("A file named 'data' already exists"));
    }
}

