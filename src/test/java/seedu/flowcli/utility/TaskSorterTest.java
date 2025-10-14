package seedu.flowcli.utility;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.utility.TaskSorter;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.TaskWithProject;

@DisplayName("TaskSorter Unit Tests")
class TaskSorterTest {

    private static final Logger logger = Logger.getLogger(TaskSorterTest.class.getName());

    private ProjectList projects;
    private Project project1;
    private Project project2;
    private Project emptyProject;

    @BeforeEach
    void setUp() {
        // Configure logging to show FINE level for this test class
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINE);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.FINE);
        }

        logger.info("Setting up test fixtures for TaskSorter tests");

        projects = new ProjectList();
        project1 = new Project("Project1");
        project2 = new Project("Project2");
        emptyProject = new Project("EmptyProject");

        // Add tasks with different priorities and deadlines
        project1.addTask("Task A", LocalDate.of(2025, 12, 31), 3); // High
                                                                   // priority,
                                                                   // late
                                                                   // deadline
        project1.addTask("Task B", LocalDate.of(2025, 10, 15), 1); // Low
                                                                   // priority,
                                                                   // early
                                                                   // deadline
        project1.addTask("Task C", null, 2); // Medium priority, no deadline

        project2.addTask("Task D", LocalDate.of(2025, 11, 20), 2); // Medium
                                                                   // priority,
                                                                   // mid
                                                                   // deadline
        project2.addTask("Task E", LocalDate.of(2025, 10, 10), 3); // High
                                                                   // priority,
                                                                   // earliest
                                                                   // deadline

        projects.getProjectList().add(project1);
        projects.getProjectList().add(project2);
        projects.getProjectList().add(emptyProject);

        logger.info("Test setup completed with " + projects.getProjectList().size() + " projects");
    }

    @Test @DisplayName("sortTasks_byDeadlineAscending_nullsLast_returnsEarliestDeadlinesFirst")
    void testSortByDeadlineAscending() {
        logger.fine("Testing sort by deadline ascending");

        TaskSorter sorter = new TaskSorter(projects, "deadline", true);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();

        assertAll("Deadline ascending sort validation",
                () -> assertEquals(5, sortedTasks.size(), "Should return all 5 tasks"),
                () -> assertEquals("Task E", sortedTasks.get(0).getTask().getDescription(),
                        "First task should be Task E (earliest deadline)"),
                () -> assertEquals("Task B", sortedTasks.get(1).getTask().getDescription(),
                        "Second task should be Task B"),
                () -> assertEquals("Task D", sortedTasks.get(2).getTask().getDescription(),
                        "Third task should be Task D"),
                () -> assertEquals("Task A", sortedTasks.get(3).getTask().getDescription(),
                        "Fourth task should be Task A"),
                () -> assertNull(sortedTasks.get(4).getTask().getDeadline(),
                        "Last task should have null deadline (Task C)"));

        logger.info("Deadline ascending sort test passed");
    }

    @Test @DisplayName("sortTasks_byDeadlineDescending_nullsFirst_returnsLatestDeadlinesFirst")
    void testSortByDeadlineDescending() {
        logger.fine("Testing sort by deadline descending");

        TaskSorter sorter = new TaskSorter(projects, "deadline", false);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();

        assertAll("Deadline descending sort validation",
                () -> assertEquals(5, sortedTasks.size(), "Should return all 5 tasks"),
                () -> assertNull(sortedTasks.get(0).getTask().getDeadline(),
                        "First task should have null deadline (Task C)"),
                () -> assertEquals("Task A", sortedTasks.get(1).getTask().getDescription(),
                        "Second task should be Task A (latest deadline)"),
                () -> assertEquals("Task D", sortedTasks.get(2).getTask().getDescription(),
                        "Third task should be Task D"),
                () -> assertEquals("Task B", sortedTasks.get(3).getTask().getDescription(),
                        "Fourth task should be Task B"),
                () -> assertEquals("Task E", sortedTasks.get(4).getTask().getDescription(),
                        "Last task should be Task E (earliest deadline)"));

        logger.info("Deadline descending sort test passed");
    }

    @Test @DisplayName("sortTasks_byPriorityAscendingAndDescending_returnsCorrectOrder")
    void testSortByPriorityBothDirections() {
        logger.fine("Testing sort by priority in both directions");

        TaskSorter ascSorter = new TaskSorter(projects, "priority", true);
        TaskSorter descSorter = new TaskSorter(projects, "priority", false);

        List<TaskWithProject> ascTasks = ascSorter.getSortedTasks();
        List<TaskWithProject> descTasks = descSorter.getSortedTasks();

        assertAll("Priority sorting both directions validation",
                () -> assertEquals(5, ascTasks.size(), "Ascending should return all 5 tasks"),
                () -> assertEquals(5, descTasks.size(), "Descending should return all 5 tasks"),

                // Ascending: Low (1) -> Medium (2) ->
                // High (3)
                () -> assertEquals("Task B", ascTasks.get(0).getTask().getDescription(),
                        "First in ascending should be low priority (Task B)"),
                () -> assertTrue(
                        ascTasks.get(1).getTask().getPriority() == 2 && ascTasks.get(2).getTask().getPriority() == 2,
                        "Second and third should be medium priority"),
                () -> assertTrue(
                        ascTasks.get(3).getTask().getPriority() == 3 && ascTasks.get(4).getTask().getPriority() == 3,
                        "Fourth and fifth should be high priority"),

                // Descending: High (3) -> Medium (2) ->
                // Low (1)
                () -> assertTrue(
                        descTasks.get(0).getTask().getPriority() == 3 && descTasks.get(1).getTask().getPriority() == 3,
                        "First and second in descending should be high priority"),
                () -> assertTrue(
                        descTasks.get(2).getTask().getPriority() == 2 && descTasks.get(3).getTask().getPriority() == 2,
                        "Third and fourth should be medium priority"),
                () -> assertEquals("Task B", descTasks.get(4).getTask().getDescription(),
                        "Last in descending should be low priority (Task B)"));

        logger.info("Priority sorting both directions test passed");
    }

    @Test @DisplayName("sortTasks_emptyProjectList_returnsEmptyList")
    void testSortEmptyProjectList() {
        logger.fine("Testing sort with empty project list");

        ProjectList emptyProjects = new ProjectList();
        TaskSorter sorter = new TaskSorter(emptyProjects, "deadline", true);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();

        assertEquals(0, sortedTasks.size(), "Should return empty list for empty project list");

        logger.info("Empty project list sort test passed");
    }

    @Test @DisplayName("sortTasks_singleTask_returnsSingleTask")
    void testSortSingleTask() {
        logger.fine("Testing sort with single task");

        ProjectList singleProjectList = new ProjectList();
        Project singleProject = new Project("SingleProject");
        singleProject.addTask("Single Task", LocalDate.of(2025, 12, 25), 2);
        singleProjectList.getProjectList().add(singleProject);

        TaskSorter sorter = new TaskSorter(singleProjectList, "deadline", true);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();

        assertAll("Single task sort validation",
                () -> assertEquals(1, sortedTasks.size(), "Should return exactly 1 task"),
                () -> assertEquals("Single Task", sortedTasks.get(0).getTask().getDescription(),
                        "Task description should match"),
                () -> assertEquals("SingleProject", sortedTasks.get(0).getProjectName(), "Project name should match"));

        logger.info("Single task sort test passed");
    }

    @Test @DisplayName("sortTasks_nullDeadlines_handledCorrectly")
    void testSortNullDeadlines() {
        logger.fine("Testing sort with multiple null deadlines");

        ProjectList nullDeadlineProjects = new ProjectList();
        Project nullProject = new Project("NullProject");

        // Add tasks with null deadlines
        nullProject.addTask("Null Task 1", null, 1);
        nullProject.addTask("Null Task 2", null, 3);
        nullProject.addTask("Dated Task", LocalDate.of(2025, 10, 10), 2);

        nullDeadlineProjects.getProjectList().add(nullProject);

        TaskSorter sorter = new TaskSorter(nullDeadlineProjects, "deadline", true);
        List<TaskWithProject> sortedTasks = sorter.getSortedTasks();

        assertAll("Null deadline sort validation",
                () -> assertEquals(3, sortedTasks.size(), "Should return all 3 tasks"),
                () -> assertNotNull(sortedTasks.get(0).getTask().getDeadline(),
                        "First task should have a deadline (Dated Task)"),
                () -> assertNull(sortedTasks.get(1).getTask().getDeadline(), "Second task should have null deadline"),
                () -> assertNull(sortedTasks.get(2).getTask().getDeadline(), "Third task should have null deadline"));

        logger.info("Null deadline sort test passed");
    }
}
