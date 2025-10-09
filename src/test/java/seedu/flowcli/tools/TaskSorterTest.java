package seedu.flowcli.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskSorterTest {

    private ProjectList projects;
    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        project1 = new Project("Project1");
        project2 = new Project("Project2");

        // Add tasks with different priorities and deadlines
        project1.addTask("Task A", LocalDate.of(2025, 12, 31), 3); // High priority, late deadline
        project1.addTask("Task B", LocalDate.of(2025, 10, 15), 1); // Low priority, early deadline
        project1.addTask("Task C", null, 2); // Medium priority, no deadline

        project2.addTask("Task D", LocalDate.of(2025, 11, 20), 2); // Medium priority, mid deadline
        project2.addTask("Task E", LocalDate.of(2025, 10, 10), 3); // High priority, earliest deadline

        projects.getProjectList().add(project1);
        projects.getProjectList().add(project2);
    }

    @Test
    void testSortByDeadlineAscending() {
        TaskSorter sorter = new TaskSorter(projects, "deadline", true);
        List<TaskSorter.SortedTask> sortedTasks = sorter.getSortedTasks();

        // Expected order: earliest deadlines first, nulls last
        assertEquals("Task E", sortedTasks.get(0).getTask().getDescription());
        assertEquals("Task B", sortedTasks.get(1).getTask().getDescription());
        assertEquals("Task D", sortedTasks.get(2).getTask().getDescription());
        assertEquals("Task A", sortedTasks.get(3).getTask().getDescription());
        assertEquals("Task C", sortedTasks.get(4).getTask().getDescription());
    }

    @Test
    void testSortByDeadlineDescending() {
        TaskSorter sorter = new TaskSorter(projects, "deadline", false);
        List<TaskSorter.SortedTask> sortedTasks = sorter.getSortedTasks();

        // Expected order: latest deadlines first, nulls first
        assertEquals("Task C", sortedTasks.get(0).getTask().getDescription());
        assertEquals("Task A", sortedTasks.get(1).getTask().getDescription());
        assertEquals("Task D", sortedTasks.get(2).getTask().getDescription());
        assertEquals("Task B", sortedTasks.get(3).getTask().getDescription());
        assertEquals("Task E", sortedTasks.get(4).getTask().getDescription());
    }

    @Test
    void testSortByPriorityAscending() {
        TaskSorter sorter = new TaskSorter(projects, "priority", true);
        List<TaskSorter.SortedTask> sortedTasks = sorter.getSortedTasks();

        // Expected order: low to high priority
        assertEquals("Task B", sortedTasks.get(0).getTask().getDescription()); // Low
        assertEquals("Task C", sortedTasks.get(1).getTask().getDescription()); // Medium
        assertEquals("Task D", sortedTasks.get(2).getTask().getDescription()); // Medium
        assertEquals("Task A", sortedTasks.get(3).getTask().getDescription()); // High
        assertEquals("Task E", sortedTasks.get(4).getTask().getDescription()); // High
    }

    @Test
    void testSortByPriorityDescending() {
        TaskSorter sorter = new TaskSorter(projects, "priority", false);
        List<TaskSorter.SortedTask> sortedTasks = sorter.getSortedTasks();

        // Expected order: high to low priority
        assertEquals("Task A", sortedTasks.get(0).getTask().getDescription()); // High
        assertEquals("Task E", sortedTasks.get(1).getTask().getDescription()); // High
        assertEquals("Task C", sortedTasks.get(2).getTask().getDescription()); // Medium
        assertEquals("Task D", sortedTasks.get(3).getTask().getDescription()); // Medium
        assertEquals("Task B", sortedTasks.get(4).getTask().getDescription()); // Low
    }
}
