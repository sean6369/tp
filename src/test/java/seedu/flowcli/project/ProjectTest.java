package seedu.flowcli.project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.task.Task;

@DisplayName("Project Unit Tests")
class ProjectTest {

    private Project project;

    @BeforeEach
    void setUp() {
        project = new Project("TestProject");
    }

    @Test
    @DisplayName("constructor_withValidName_createsProject")
    void constructorWithValidName() {
        Project p = new Project("MyProject");

        assertAll("Project creation",
                () -> assertEquals("MyProject", p.getProjectName()),
                () -> assertTrue(p.isEmpty(), "Project should start empty"),
                () -> assertEquals(0, p.size()),
                () -> assertNotNull(p.getProjectTasks(), "Task list should not be null")
        );
    }

    @Test
    @DisplayName("constructor_withNullName_throwsIllegalArgumentException")
    void constructorWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Project(null),
                "Should throw when project name is null");
    }

    @Test
    @DisplayName("constructor_withEmptyName_throwsIllegalArgumentException")
    void constructorWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Project(""),
                "Should throw when project name is empty");
        assertThrows(IllegalArgumentException.class, () -> new Project("   "),
                "Should throw when project name is whitespace");
    }

    @Test
    @DisplayName("addTask_withDescriptionOnly_addsTask")
    void addTaskWithDescriptionOnly() {
        project.addTask("Task 1");

        assertAll("Add task",
                () -> assertEquals(1, project.size()),
                () -> assertFalse(project.isEmpty()),
                () -> assertEquals("Task 1", project.getProjectTasks().get(0).getDescription())
        );
    }

    @Test
    @DisplayName("addTask_withAllParameters_addsTaskWithDetails")
    void addTaskWithAllParameters() throws Exception {
        LocalDate deadline = LocalDate.of(2024, 12, 31);
        project.addTask("Important task", deadline, 3);

        Task task = project.getProjectTasks().get(0);
        assertAll("Add task with details",
                () -> assertEquals(1, project.size()),
                () -> assertEquals("Important task", task.getDescription()),
                () -> assertEquals(deadline, task.getDeadline()),
                () -> assertEquals(3, task.getPriority())
        );
    }

    @Test
    @DisplayName("addTask_multipleTasks_increasesSize")
    void addMultipleTasks() {
        project.addTask("Task 1");
        project.addTask("Task 2");
        project.addTask("Task 3");

        assertEquals(3, project.size());
    }

    @Test
    @DisplayName("deleteTask_validIndex_removesAndReturnsTask")
    void deleteTaskValidIndex() throws Exception {
        project.addTask("Task 1");
        project.addTask("Task 2");

        Task deletedTask = project.deleteTask(0);

        assertAll("Delete task",
                () -> assertEquals("Task 1", deletedTask.getDescription()),
                () -> assertEquals(1, project.size()),
                () -> assertEquals("Task 2", project.getProjectTasks().get(0).getDescription())
        );
    }

    @Test
    @DisplayName("deleteTask_invalidIndex_throwsIndexOutOfRangeException")
    void deleteTaskInvalidIndex() {
        project.addTask("Task 1");

        assertThrows(IndexOutOfRangeException.class, () -> project.deleteTask(5),
                "Should throw when index is out of range");
    }

    @Test
    @DisplayName("updateTask_description_updatesTask")
    void updateTaskDescription() throws Exception {
        project.addTask("Old description");

        Task updatedTask = project.updateTask(0, "New description", true, null, false, null, false);

        assertEquals("New description", updatedTask.getDescription());
    }

    @Test
    @DisplayName("updateTask_deadline_updatesDeadline")
    void updateTaskDeadline() throws Exception {
        project.addTask("Task 1");
        LocalDate newDeadline = LocalDate.of(2025, 1, 1);

        Task updatedTask = project.updateTask(0, null, false, newDeadline, true, null, false);

        assertEquals(newDeadline, updatedTask.getDeadline());
    }

    @Test
    @DisplayName("updateTask_priority_updatesPriority")
    void updateTaskPriority() throws Exception {
        project.addTask("Task 1");

        Task updatedTask = project.updateTask(0, null, false, null, false, 3, true);

        assertEquals(3, updatedTask.getPriority());
    }

    @Test
    @DisplayName("updateTask_multipleFields_updatesAll")
    void updateTaskMultipleFields() throws Exception {
        project.addTask("Old task", LocalDate.of(2024, 1, 1), 1);
        LocalDate newDeadline = LocalDate.of(2025, 12, 31);

        Task updatedTask = project.updateTask(0, "New task", true, newDeadline, true, 3, true);

        assertAll("Update multiple fields",
                () -> assertEquals("New task", updatedTask.getDescription()),
                () -> assertEquals(newDeadline, updatedTask.getDeadline()),
                () -> assertEquals(3, updatedTask.getPriority())
        );
    }

    @Test
    @DisplayName("updateTask_noUpdates_returnsUnchangedTask")
    void updateTaskNoUpdates() throws Exception {
        project.addTask("Task 1", LocalDate.of(2024, 1, 1), 2);

        Task updatedTask = project.updateTask(0, null, false, null, false, null, false);

        assertAll("No updates",
                () -> assertEquals("Task 1", updatedTask.getDescription()),
                () -> assertEquals(LocalDate.of(2024, 1, 1), updatedTask.getDeadline()),
                () -> assertEquals(2, updatedTask.getPriority())
        );
    }

    @Test
    @DisplayName("updateTask_invalidIndex_throwsIndexOutOfRangeException")
    void updateTaskInvalidIndex() {
        project.addTask("Task 1");

        assertThrows(IndexOutOfRangeException.class,
                () -> project.updateTask(10, "New", true, null, false, null, false),
                "Should throw when index is invalid");
    }

    @Test
    @DisplayName("isEmpty_correctlyReflectsState")
    void isEmptyReflectsState() throws Exception {
        assertTrue(project.isEmpty());

        project.addTask("Task 1");
        assertFalse(project.isEmpty());

        project.deleteTask(0);
        assertTrue(project.isEmpty());
    }

    @Test
    @DisplayName("size_correctlyTracksTaskCount")
    void sizeTracksCount() throws Exception {
        assertEquals(0, project.size());

        project.addTask("Task 1");
        assertEquals(1, project.size());

        project.addTask("Task 2");
        assertEquals(2, project.size());

        project.deleteTask(0);
        assertEquals(1, project.size());
    }

    @Test
    @DisplayName("getProjectName_returnsCorrectName")
    void getProjectNameReturnsName() {
        assertEquals("TestProject", project.getProjectName());

        Project p2 = new Project("Another Project");
        assertEquals("Another Project", p2.getProjectName());
    }

    @Test
    @DisplayName("getProjectTasks_returnsTaskList")
    void getProjectTasksReturnsTaskList() {
        assertNotNull(project.getProjectTasks());
        assertEquals(0, project.getProjectTasks().size());

        project.addTask("Task 1");
        assertEquals(1, project.getProjectTasks().size());
    }

    @Test
    @DisplayName("toString_includesProjectNameAndTasks")
    void toStringFormat() {
        project.addTask("Task 1");
        project.addTask("Task 2");

        String result = project.toString();

        assertTrue(result.contains("TestProject"));
        assertTrue(result.contains("Task 1"));
        assertTrue(result.contains("Task 2"));
    }

    @Test
    @DisplayName("showAllTasks_rendersTaskList")
    void showAllTasksRenders() {
        project.addTask("Task 1");
        project.addTask("Task 2");

        String result = project.showAllTasks();

        assertTrue(result.contains("1. [ ] Task 1"));
        assertTrue(result.contains("2. [ ] Task 2"));
    }

    @Test
    @DisplayName("showAllTasks_emptyProject_returnsEmptyString")
    void showAllTasksEmpty() {
        String result = project.showAllTasks();

        assertEquals("", result);
    }
}

