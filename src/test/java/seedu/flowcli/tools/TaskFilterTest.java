package seedu.flowcli.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskFilterTest {

    private ProjectList projects;
    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        project1 = new Project("Project1");
        project2 = new Project("Project2");

        // Add tasks with different priorities
        project1.addTask("Task A", null, 3); // High
        project1.addTask("Task B", null, 1); // Low
        project1.addTask("Task C", null, 2); // Medium

        project2.addTask("Task D", null, 2); // Medium
        project2.addTask("Task E", null, 3); // High

        projects.getProjectList().add(project1);
        projects.getProjectList().add(project2);
    }

    @Test
    void testFilterByPriorityHigh() {
        TaskFilter filter = new TaskFilter(projects, "high", null);
        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();

        assertEquals(2, filteredTasks.size());
        assertEquals("Task A", filteredTasks.get(0).getTask().getDescription());
        assertEquals("Task E", filteredTasks.get(1).getTask().getDescription());
    }

    @Test
    void testFilterByPriorityMedium() {
        TaskFilter filter = new TaskFilter(projects, "medium", null);
        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();

        assertEquals(2, filteredTasks.size());
        assertEquals("Task C", filteredTasks.get(0).getTask().getDescription());
        assertEquals("Task D", filteredTasks.get(1).getTask().getDescription());
    }

    @Test
    void testFilterByPriorityLow() {
        TaskFilter filter = new TaskFilter(projects, "low", null);
        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();

        assertEquals(1, filteredTasks.size());
        assertEquals("Task B", filteredTasks.get(0).getTask().getDescription());
    }

    @Test
    void testFilterByProjectId() {
        TaskFilter filter = new TaskFilter(projects, null, 1); // Project1
        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();

        assertEquals(3, filteredTasks.size());
        for (TaskFilter.FilteredTask ft : filteredTasks) {
            assertEquals("Project1", ft.getProjectName());
        }
    }

    @Test
    void testFilterByProjectId2() {
        TaskFilter filter = new TaskFilter(projects, null, 2); // Project2
        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();

        assertEquals(2, filteredTasks.size());
        for (TaskFilter.FilteredTask ft : filteredTasks) {
            assertEquals("Project2", ft.getProjectName());
        }
    }

    @Test
    void testFilterByPriorityAndProject() {
        TaskFilter filter = new TaskFilter(projects, "high", 1); // High priority in Project1
        List<TaskFilter.FilteredTask> filteredTasks = filter.getFilteredTasks();

        assertEquals(1, filteredTasks.size());
        assertEquals("Task A", filteredTasks.get(0).getTask().getDescription());
        assertEquals("Project1", filteredTasks.get(0).getProjectName());
    }
}