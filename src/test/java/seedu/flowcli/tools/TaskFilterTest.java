package seedu.flowcli.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.TaskWithProject;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TaskFilter Unit Tests")
class TaskFilterTest {

    private static final Logger logger = Logger.getLogger(TaskFilterTest.class.getName());

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

        logger.info("Setting up test fixtures for TaskFilter tests");

        projects = new ProjectList();
        project1 = new Project("Project1");
        project2 = new Project("Project2");
        emptyProject = new Project("EmptyProject");

        // Add tasks with different priorities
        project1.addTask("Task A", null, 3); // High priority
        project1.addTask("Task B", null, 1); // Low priority
        project1.addTask("Task C", null, 2); // Medium priority

        project2.addTask("Task D", null, 2); // Medium priority
        project2.addTask("Task E", null, 3); // High priority

        projects.getProjectList().add(project1);
        projects.getProjectList().add(project2);
        projects.getProjectList().add(emptyProject);

        logger.info("Test setup completed with " + projects.getProjectList().size() + " projects");
    }

    @Test
    @DisplayName("filterTasks_byAllPriorities_returnsCorrectTasksForEachLevel")
    void testFilterByAllPriorities() {
        logger.fine("Testing filter by all priority levels");

        // Test HIGH priority
        TaskFilter highFilter = new TaskFilter(projects, "high", null);
        List<TaskWithProject> highTasks = highFilter.getFilteredTasks();

        // Test MEDIUM priority
        TaskFilter mediumFilter = new TaskFilter(projects, "medium", null);
        List<TaskWithProject> mediumTasks = mediumFilter.getFilteredTasks();

        // Test LOW priority
        TaskFilter lowFilter = new TaskFilter(projects, "low", null);
        List<TaskWithProject> lowTasks = lowFilter.getFilteredTasks();

        assertAll("All priority levels validation",
                () -> assertEquals(2, highTasks.size(), "High priority should return 2 tasks"),
                () -> assertEquals(2, mediumTasks.size(), "Medium priority should return 2 tasks"),
                () -> assertEquals(1, lowTasks.size(), "Low priority should return 1 task"),
                () -> assertTrue(highTasks.stream().allMatch(ft -> ft.getTask().getPriority() == 3),
                        "All high priority tasks should have priority 3"),
                () -> assertTrue(mediumTasks.stream().allMatch(ft -> ft.getTask().getPriority() == 2),
                        "All medium priority tasks should have priority 2"),
                () -> assertTrue(lowTasks.stream().allMatch(ft -> ft.getTask().getPriority() == 1),
                        "All low priority tasks should have priority 1"));

        logger.info("All priority levels filter test passed");
    }

    @Test
    @DisplayName("filterTasks_byProjectNameProject1_returnsOnlyProject1Tasks")
    void testFilterByProjectName() {
        logger.fine("Testing filter by project name: Project1");

        TaskFilter filter = new TaskFilter(projects, null, "Project1");
        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();

        assertAll("Project1 filter validation",
                () -> assertEquals(3, filteredTasks.size(), "Should return exactly 3 tasks from Project1"),
                () -> assertTrue(filteredTasks.stream().allMatch(ft -> "Project1".equals(ft.getProjectName())),
                        "All returned tasks should belong to Project1"));

        logger.info("Project1 filter test passed");
    }

    @Test
    @DisplayName("filterTasks_byProjectNameCaseInsensitive_worksWithDifferentCases")
    void testFilterByProjectNameCaseInsensitive() {
        logger.fine("Testing case-insensitive project name filtering");

        // Test different case variations
        TaskFilter upperFilter = new TaskFilter(projects, null, "PROJECT1");
        TaskFilter lowerFilter = new TaskFilter(projects, null, "project1");
        TaskFilter mixedFilter = new TaskFilter(projects, null, "PrOjEcT1");

        List<TaskWithProject> upperTasks = upperFilter.getFilteredTasks();
        List<TaskWithProject> lowerTasks = lowerFilter.getFilteredTasks();
        List<TaskWithProject> mixedTasks = mixedFilter.getFilteredTasks();

        assertAll("Case-insensitive project filtering validation",
                () -> assertEquals(3, upperTasks.size(), "UPPERCASE should return 3 tasks"),
                () -> assertEquals(3, lowerTasks.size(), "lowercase should return 3 tasks"),
                () -> assertEquals(3, mixedTasks.size(), "Mixed case should return 3 tasks"),
                () -> assertTrue(upperTasks.stream().allMatch(ft -> "Project1".equals(ft.getProjectName())),
                        "All tasks should belong to Project1 regardless of case"));

        logger.info("Case-insensitive project filtering test passed");
    }

    @Test
    @DisplayName("filterTasks_byPriorityHighAndProject1_returnsHighPriorityTasksFromProject1")
    void testFilterByPriorityAndProject() {
        logger.fine("Testing combined filter: high priority in Project1");

        TaskFilter filter = new TaskFilter(projects, "high", "Project1");
        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();

        assertAll("Combined filter validation",
                () -> assertEquals(1, filteredTasks.size(), "Should return exactly 1 task"),
                () -> assertEquals("Task A", filteredTasks.get(0).getTask().getDescription(),
                        "Task should be Task A"),
                () -> assertEquals("Project1", filteredTasks.get(0).getProjectName(),
                        "Task should belong to Project1"),
                () -> assertEquals(3, filteredTasks.get(0).getTask().getPriority(),
                        "Task should have high priority"));

        logger.info("Combined filter test passed");
    }

    @Test
    @DisplayName("filterTasks_byNonExistentProject_returnsEmptyList")
    void testFilterByNonExistentProject() {
        logger.fine("Testing filter by non-existent project");

        TaskFilter filter = new TaskFilter(projects, null, "NonExistentProject");
        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();

        assertEquals(0, filteredTasks.size(), "Should return empty list for non-existent project");

        logger.info("Non-existent project filter test passed");
    }

    @Test
    @DisplayName("filterTasks_byInvalidPriority_throwsNoException")
    void testFilterByInvalidPriority() {
        logger.fine("Testing filter by invalid priority");

        // Invalid priority should not crash, just return empty results
        TaskFilter filter = new TaskFilter(projects, "invalid", null);
        List<TaskWithProject> filteredTasks = filter.getFilteredTasks();

        assertEquals(0, filteredTasks.size(), "Should return empty list for invalid priority");

        logger.info("Invalid priority filter test passed");
    }

    @Test
    @DisplayName("filterTasks_withNullInputs_handlesGracefully")
    void testFilterWithNullInputs() {
        logger.fine("Testing filter with null inputs");

        // Test null project list (should throw assertion error in constructor)
        assertThrows(AssertionError.class, () -> new TaskFilter((ProjectList) null, "high", null),
                "Null project list should trigger assertion error");

        // Test with valid project list but null/empty strings
        TaskFilter emptyPriorityFilter = new TaskFilter(projects, "", null);
        TaskFilter emptyProjectFilter = new TaskFilter(projects, null, "");

        List<TaskWithProject> emptyPriorityTasks = emptyPriorityFilter.getFilteredTasks();
        List<TaskWithProject> emptyProjectTasks = emptyProjectFilter.getFilteredTasks();

        assertAll("Null/empty input validation",
                () -> assertEquals(0, emptyPriorityTasks.size(), "Empty priority should return no tasks"),
                () -> assertEquals(0, emptyProjectTasks.size(), "Empty project name should return no tasks"));

        logger.info("Null input handling test passed");
    }

    @Test
    @DisplayName("filterTasks_withSpecialCharactersAndLongNames_handlesEdgeCases")
    void testFilterWithSpecialCharactersAndLongNames() {
        logger.fine("Testing filter with special characters and long names");

        // Create projects with special characters and long names
        ProjectList specialProjects = new ProjectList();
        Project specialProject = new Project("Project-With-Dashes_And_Underscores123");
        Project longNameProject = new Project("VeryLongProjectNameThatExceedsNormalLengthAndContainsManyCharacters");

        specialProject.addTask("Special Task", null, 1);
        longNameProject.addTask("Long Name Task", null, 2);

        specialProjects.getProjectList().add(specialProject);
        specialProjects.getProjectList().add(longNameProject);

        // Test filtering by special character project name
        TaskFilter specialFilter = new TaskFilter(specialProjects, null, "project-with-dashes_and_underscores123");
        List<TaskWithProject> specialTasks = specialFilter.getFilteredTasks();

        // Test filtering by long project name
        TaskFilter longFilter = new TaskFilter(specialProjects, null,
                "verylongprojectnamethatexceedsnormallengthandcontainsmanycharacters");
        List<TaskWithProject> longTasks = longFilter.getFilteredTasks();

        assertAll("Special characters and long names validation",
                () -> assertEquals(1, specialTasks.size(), "Special character project should return 1 task"),
                () -> assertEquals(1, longTasks.size(), "Long name project should return 1 task"),
                () -> assertEquals("Project-With-Dashes_And_Underscores123", specialTasks.get(0).getProjectName(),
                        "Special character project name should match"),
                () -> assertEquals("VeryLongProjectNameThatExceedsNormalLengthAndContainsManyCharacters",
                        longTasks.get(0).getProjectName(),
                        "Long project name should match"));

        logger.info("Special characters and long names test passed");
    }
}
