package seedu.flowcli.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Task Unit Tests")
class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Sample task");
    }

    @Test
    @DisplayName("constructor_withDescriptionOnly_createsTaskWithDefaults")
    void constructorWithDescriptionOnly() {
        Task t = new Task("Test task");

        assertAll("Task with defaults",
                () -> assertEquals("Test task", t.getDescription()),
                () -> assertFalse(t.isDone(), "Task should not be done by default"),
                () -> assertNull(t.getDeadline(), "Deadline should be null by default"),
                () -> assertEquals(2, t.getPriority(), "Default priority should be 2 (medium)")
        );
    }

    @Test
    @DisplayName("constructor_withAllParameters_createsTaskWithValues")
    void constructorWithAllParameters() {
        LocalDate deadline = LocalDate.of(2024, 12, 31);
        Task t = new Task("Complete project", deadline, 3);

        assertAll("Task with all parameters",
                () -> assertEquals("Complete project", t.getDescription()),
                () -> assertFalse(t.isDone()),
                () -> assertEquals(deadline, t.getDeadline()),
                () -> assertEquals(3, t.getPriority())
        );
    }

    @Test
    @DisplayName("mark_changesStatusToDone")
    void markChangesStatus() {
        assertFalse(task.isDone(), "Task should start as not done");

        task.mark();

        assertTrue(task.isDone(), "Task should be marked as done");
        assertTrue(task.getDone(), "getDone() should also return true");
    }

    @Test
    @DisplayName("unmark_changesStatusToNotDone")
    void unmarkChangesStatus() {
        task.mark();
        assertTrue(task.isDone(), "Task should be done");

        task.unmark();

        assertFalse(task.isDone(), "Task should be unmarked");
    }

    @Test
    @DisplayName("marker_returnsCorrectSymbol")
    void markerReturnsCorrectSymbol() {
        assertEquals("[ ]", task.marker(), "Unmarked task should show [ ]");

        task.mark();
        assertEquals("[X]", task.marker(), "Marked task should show [X]");
    }

    @Test
    @DisplayName("setDescription_updatesDescription")
    void setDescriptionUpdates() {
        task.setDescription("New description");

        assertEquals("New description", task.getDescription());
    }

    @Test
    @DisplayName("setDeadline_updatesDeadline")
    void setDeadlineUpdates() {
        LocalDate newDeadline = LocalDate.of(2025, 1, 1);
        
        task.setDeadline(newDeadline);

        assertEquals(newDeadline, task.getDeadline());
    }

    @Test
    @DisplayName("setPriority_updatesPriority")
    void setPriorityUpdates() {
        task.setPriority(3);

        assertEquals(3, task.getPriority());
    }

    @Test
    @DisplayName("getPriorityString_returnsCorrectLabels")
    void getPriorityStringReturnsLabels() {
        task.setPriority(1);
        assertEquals("Low", task.getPriorityString());

        task.setPriority(2);
        assertEquals("Medium", task.getPriorityString());

        task.setPriority(3);
        assertEquals("High", task.getPriorityString());
    }

    @Test
    @DisplayName("getPriorityString_invalidPriority_returnsUnknown")
    void getPriorityStringInvalidReturnsUnknown() {
        task.setPriority(99);

        assertEquals("Unknown", task.getPriorityString());
    }

    @Test
    @DisplayName("toString_withNoDeadline_showsDescriptionAndPriority")
    void toStringWithNoDeadline() {
        task.setPriority(2);
        String result = task.toString();

        assertTrue(result.contains("[ ]"), "Should contain marker");
        assertTrue(result.contains("Sample task"), "Should contain description");
        assertTrue(result.contains("[Medium]"), "Should contain priority");
        assertFalse(result.contains("Due:"), "Should not contain deadline");
    }

    @Test
    @DisplayName("toString_withDeadline_showsAll")
    void toStringWithDeadline() {
        LocalDate deadline = LocalDate.of(2024, 12, 31);
        Task t = new Task("Important task", deadline, 3);

        String result = t.toString();

        assertAll("ToString with deadline",
                () -> assertTrue(result.contains("[ ]"), "Should contain marker"),
                () -> assertTrue(result.contains("Important task"), "Should contain description"),
                () -> assertTrue(result.contains("Due:"), "Should contain deadline label"),
                () -> assertTrue(result.contains("Dec 31, 2024"), "Should contain formatted date"),
                () -> assertTrue(result.contains("[High]"), "Should contain priority")
        );
    }

    @Test
    @DisplayName("toString_markedTask_showsXMarker")
    void toStringMarkedTask() {
        task.mark();
        String result = task.toString();

        assertTrue(result.contains("[X]"), "Marked task should show [X]");
    }

    @Test
    @DisplayName("isDone_initiallyFalse")
    void isDoneInitiallyFalse() {
        Task newTask = new Task("New task");

        assertFalse(newTask.isDone());
        assertFalse(newTask.getDone());
    }

    @Test
    @DisplayName("multipleMarkUnmark_changesStatusCorrectly")
    void multipleMarkUnmarkChangesStatus() {
        assertFalse(task.isDone());

        task.mark();
        assertTrue(task.isDone());

        task.unmark();
        assertFalse(task.isDone());

        task.mark();
        assertTrue(task.isDone());

        task.mark(); // marking again
        assertTrue(task.isDone(), "Marking again should keep it done");
    }
}

