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
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        new File(testFilePath).delete();
        new File(testFilePath + ".backup").delete();
    }

    @Test
    void save_emptyProjectList_success() throws StorageException, DataCorruptedException {
        ProjectList projects = new ProjectList();
        storage.save(projects);
        
        ProjectList loaded = storage.load();
        assertEquals(0, loaded.getProjectListSize());
    }

    @Test
    void save_projectWithTasks_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("CS2113");
        Project project = projects.getProjectByIndex(0);
        project.addTask("Assignment", LocalDate.of(2025, 12, 31), 3);
        project.addTask("Study");
        project.getProjectTasks().mark(0);
        
        storage.save(projects);
        ProjectList loaded = storage.load();
        
        assertEquals(1, loaded.getProjectListSize());
        Project loadedProject = loaded.getProjectByIndex(0);
        assertEquals("CS2113", loadedProject.getProjectName());
        assertEquals(2, loadedProject.size());
        assertEquals("Assignment", loadedProject.getProjectTasks().get(0).getDescription());
        assertTrue(loadedProject.getProjectTasks().get(0).isDone());
    }

    @Test
    void save_multipleProjects_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Project A");
        projects.addProject("Project B");
        projects.getProjectByIndex(0).addTask("Task A");
        projects.getProjectByIndex(1).addTask("Task B");
        
        storage.save(projects);
        ProjectList loaded = storage.load();
        
        assertEquals(2, loaded.getProjectListSize());
        assertEquals("Project A", loaded.getProjectByIndex(0).getProjectName());
        assertEquals("Project B", loaded.getProjectByIndex(1).getProjectName());
    }

    @Test
    void save_specialCharacters_success() throws Exception {
        ProjectList projects = new ProjectList();
        projects.addProject("Test | Project");
        projects.getProjectByIndex(0).addTask("Task | with pipe");
        
        storage.save(projects);
        ProjectList loaded = storage.load();
        
        assertEquals("Test | Project", loaded.getProjectByIndex(0).getProjectName());
        assertEquals("Task | with pipe", 
            loaded.getProjectByIndex(0).getProjectTasks().get(0).getDescription());
    }

    @Test
    void load_fileNotExists_returnsEmptyList() throws Exception {
        ProjectList loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(0, loaded.getProjectListSize());
    }

    @Test
    void load_corruptedFormat_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("INVALID|LINE\n");
        }
        
        assertThrows(DataCorruptedException.class, () -> storage.load());
        
        // Verify backup was created
        assertTrue(new File(testFilePath + ".backup").exists());
    }

    @Test
    void load_taskWithoutProject_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("TASK|0|Some task|null|2\n");
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Task found without a project"));
    }

    @Test
    void load_invalidTaskFormat_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Some task\n"); // Missing fields
        }
        
        assertThrows(DataCorruptedException.class, () -> storage.load());
    }

    @Test
    void load_invalidDateFormat_throwsException() throws IOException {
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("PROJECT|Test Project\n");
            writer.write("TASK|0|Task|bad-date|2\n");
        }
        
        DataCorruptedException exception = assertThrows(
            DataCorruptedException.class, 
            () -> storage.load()
        );
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    @Test
    void saveThenLoad_preservesAllData() throws Exception {
        ProjectList original = new ProjectList();
        original.addProject("Work");
        original.addProject("Personal");
        
        Project work = original.getProjectByIndex(0);
        work.addTask("Meeting", LocalDate.of(2025, 12, 25), 3);
        work.getProjectTasks().mark(0);
        
        Project personal = original.getProjectByIndex(1);
        personal.addTask("Shopping");
        
        storage.save(original);
        ProjectList loaded = storage.load();
        
        assertEquals(2, loaded.getProjectListSize());
        assertEquals("Work", loaded.getProjectByIndex(0).getProjectName());
        assertEquals(1, loaded.getProjectByIndex(0).size());
        assertTrue(loaded.getProjectByIndex(0).getProjectTasks().get(0).isDone());
        assertEquals("Personal", loaded.getProjectByIndex(1).getProjectName());
    }
}
