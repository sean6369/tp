package seedu.flowcli.task;

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

@DisplayName("TaskList Unit Tests")
class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
    }

    @Test
    @DisplayName("constructor_createsEmptyTaskList")
    void constructorCreatesEmpty() {
        TaskList tl = new TaskList();

        assertTrue(tl.isEmpty(), "New task list should be empty");
        assertEquals(0, tl.size(), "Size should be 0");
    }

    @Test
    @DisplayName("addTask_withDescriptionOnly_addsTask")
    void addTaskWithDescriptionOnly() {
        taskList.addTask("Task 1");

        assertAll("Add task",
                () -> assertEquals(1, taskList.size()),
                () -> assertFalse(taskList.isEmpty()),
                () -> assertEquals("Task 1", taskList.get(0).getDescription())
        );
    }

    @Test
    @DisplayName("addTask_withAllParameters_addsTaskWithDetails")
    void addTaskWithAllParameters() throws Exception {
        LocalDate deadline = LocalDate.of(2024, 12, 31);
        taskList.addTask("Complete project", deadline, 3);

        Task task = taskList.get(0);
        assertAll("Add task with details",
                () -> assertEquals(1, taskList.size()),
                () -> assertEquals("Complete project", task.getDescription()),
                () -> assertEquals(deadline, task.getDeadline()),
                () -> assertEquals(3, task.getPriority())
        );
    }

    @Test
    @DisplayName("addTask_multipleTasks_increasesSize")
    void addMultipleTasks() {
        taskList.addTask("Task 1");
        taskList.addTask("Task 2");
        taskList.addTask("Task 3");

        assertEquals(3, taskList.size());
        assertFalse(taskList.isEmpty());
    }

    @Test
    @DisplayName("get_validIndex_returnsTask")
    void getValidIndex() throws Exception {
        taskList.addTask("Task 1");
        taskList.addTask("Task 2");

        Task task1 = taskList.get(0);
        Task task2 = taskList.get(1);

        assertEquals("Task 1", task1.getDescription());
        assertEquals("Task 2", task2.getDescription());
    }

    @Test
    @DisplayName("get_invalidIndex_throwsIndexOutOfRangeException")
    void getInvalidIndex() {
        taskList.addTask("Task 1");

        assertThrows(IndexOutOfRangeException.class, () -> taskList.get(1),
                "Should throw when index is out of range");
        assertThrows(IndexOutOfRangeException.class, () -> taskList.get(-1),
                "Should throw when index is negative");
    }

    @Test
    @DisplayName("mark_validIndex_marksTask")
    void markValidIndex() throws Exception {
        taskList.addTask("Task 1");

        taskList.mark(0);

        assertTrue(taskList.get(0).isDone(), "Task should be marked as done");
    }

    @Test
    @DisplayName("mark_invalidIndex_throwsIndexOutOfRangeException")
    void markInvalidIndex() {
        taskList.addTask("Task 1");

        assertThrows(IndexOutOfRangeException.class, () -> taskList.mark(5));
    }

    @Test
    @DisplayName("unmark_validIndex_unmarksTask")
    void unmarkValidIndex() throws Exception {
        taskList.addTask("Task 1");
        taskList.mark(0);
        assertTrue(taskList.get(0).isDone());

        taskList.unmark(0);

        assertFalse(taskList.get(0).isDone(), "Task should be unmarked");
    }

    @Test
    @DisplayName("unmark_invalidIndex_throwsIndexOutOfRangeException")
    void unmarkInvalidIndex() {
        taskList.addTask("Task 1");

        assertThrows(IndexOutOfRangeException.class, () -> taskList.unmark(10));
    }

    @Test
    @DisplayName("delete_validIndex_removesTask")
    void deleteValidIndex() throws Exception {
        taskList.addTask("Task 1");
        taskList.addTask("Task 2");
        assertEquals(2, taskList.size());

        Task deletedTask = taskList.delete(0);

        assertAll("Delete task",
                () -> assertEquals("Task 1", deletedTask.getDescription()),
                () -> assertEquals(1, taskList.size()),
                () -> assertEquals("Task 2", taskList.get(0).getDescription())
        );
    }

    @Test
    @DisplayName("delete_invalidIndex_throwsIndexOutOfRangeException")
    void deleteInvalidIndex() {
        taskList.addTask("Task 1");

        assertThrows(IndexOutOfRangeException.class, () -> taskList.delete(5));
    }

    @Test
    @DisplayName("getTasks_returnsInternalList")
    void getTasksReturnsInternalList() {
        taskList.addTask("Task 1");
        taskList.addTask("Task 2");

        assertNotNull(taskList.getTasks());
        assertEquals(2, taskList.getTasks().size());
    }

    @Test
    @DisplayName("sortByDeadline_ascending_sortsCorrectly")
    void sortByDeadlineAscending() throws Exception {
        taskList.addTask("Task 1", LocalDate.of(2024, 12, 31), 2);
        taskList.addTask("Task 2", LocalDate.of(2024, 1, 1), 2);
        taskList.addTask("Task 3", LocalDate.of(2024, 6, 15), 2);

        taskList.sortByDeadline(true);

        assertAll("Sort by deadline ascending",
                () -> assertEquals(LocalDate.of(2024, 1, 1), taskList.get(0).getDeadline()),
                () -> assertEquals(LocalDate.of(2024, 6, 15), taskList.get(1).getDeadline()),
                () -> assertEquals(LocalDate.of(2024, 12, 31), taskList.get(2).getDeadline())
        );
    }

    @Test
    @DisplayName("sortByDeadline_descending_sortsCorrectly")
    void sortByDeadlineDescending() throws Exception {
        taskList.addTask("Task 1", LocalDate.of(2024, 1, 1), 2);
        taskList.addTask("Task 2", LocalDate.of(2024, 12, 31), 2);

        taskList.sortByDeadline(false);

        assertAll("Sort by deadline descending",
                () -> assertEquals(LocalDate.of(2024, 12, 31), taskList.get(0).getDeadline()),
                () -> assertEquals(LocalDate.of(2024, 1, 1), taskList.get(1).getDeadline())
        );
    }

    @Test
    @DisplayName("sortByDeadline_withNullDeadlines_handlesCorrectly")
    void sortByDeadlineWithNulls() throws Exception {
        taskList.addTask("Task with deadline", LocalDate.of(2024, 12, 31), 2);
        taskList.addTask("Task without deadline", null, 2);

        taskList.sortByDeadline(true);

        // Task with deadline should come first
        assertEquals(LocalDate.of(2024, 12, 31), taskList.get(0).getDeadline());
        assertEquals(null, taskList.get(1).getDeadline());
    }

    @Test
    @DisplayName("sortByPriority_ascending_sortsCorrectly")
    void sortByPriorityAscending() throws Exception {
        taskList.addTask("Task 1", null, 3); // high
        taskList.addTask("Task 2", null, 1); // low
        taskList.addTask("Task 3", null, 2); // medium

        taskList.sortByPriority(true);

        assertAll("Sort by priority ascending",
                () -> assertEquals(1, taskList.get(0).getPriority()),
                () -> assertEquals(2, taskList.get(1).getPriority()),
                () -> assertEquals(3, taskList.get(2).getPriority())
        );
    }

    @Test
    @DisplayName("sortByPriority_descending_sortsCorrectly")
    void sortByPriorityDescending() throws Exception {
        taskList.addTask("Task 1", null, 1); // low
        taskList.addTask("Task 2", null, 3); // high

        taskList.sortByPriority(false);

        assertAll("Sort by priority descending",
                () -> assertEquals(3, taskList.get(0).getPriority()),
                () -> assertEquals(1, taskList.get(1).getPriority())
        );
    }

    @Test
    @DisplayName("render_formatsTasksCorrectly")
    void renderFormatsCorrectly() {
        taskList.addTask("Task 1");
        taskList.addTask("Task 2");

        String rendered = taskList.render();

        assertTrue(rendered.contains("1. [ ] Task 1"));
        assertTrue(rendered.contains("2. [ ] Task 2"));
    }

    @Test
    @DisplayName("render_emptyList_returnsEmptyString")
    void renderEmptyList() {
        String rendered = taskList.render();

        assertEquals("", rendered);
    }

    @Test
    @DisplayName("isEmpty_correctlyReflectsState")
    void isEmptyReflectsState() throws Exception {
        assertTrue(taskList.isEmpty());

        taskList.addTask("Task 1");
        assertFalse(taskList.isEmpty());

        taskList.delete(0);
        assertTrue(taskList.isEmpty());
    }

    @Test
    @DisplayName("size_correctlyTracksTaskCount")
    void sizeTracksCount() throws Exception {
        assertEquals(0, taskList.size());

        taskList.addTask("Task 1");
        assertEquals(1, taskList.size());

        taskList.addTask("Task 2");
        assertEquals(2, taskList.size());

        taskList.delete(0);
        assertEquals(1, taskList.size());
    }
}

