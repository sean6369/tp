package seedu.flowcli.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.project.Project;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.task.Task;
import seedu.flowcli.ui.ConsoleUi;

class TaskCommandsTest {

    private ProjectList projects;
    private Project project;
    private StubConsoleUi ui;
    private CommandContext context;

    @BeforeEach
    void setUp() {
        projects = new ProjectList();
        projects.addProject("Alpha");
        project = projects.getProject("Alpha");
        ui = new StubConsoleUi(projects);
        context = new CommandContext(projects, ui, null);
    }

    @Test
    void addCommandAddsTaskToProject() throws Exception {
        AddCommand command = new AddCommand("Alpha Finish docs --priority high --deadline 2024-12-25");

        boolean shouldContinue = command.execute(context);

        assertTrue(shouldContinue);
        assertEquals(1, project.size());
        Task addedTask = project.getProjectTasks().get(0);
        assertEquals("Finish docs", addedTask.getDescription());
        assertEquals(LocalDate.of(2024, 12, 25), addedTask.getDeadline());
        assertEquals(3, addedTask.getPriority());
        assertFalse(addedTask.isDone());
        assertEquals("added", ui.getLastAction());
        assertSame(addedTask, ui.getLastTask());
        assertSame(project, ui.getLastProject());
    }

    @Test
    void markCommandMarksTaskAsDone() throws Exception {
        project.addTask("Initial task");
        MarkCommand command = new MarkCommand("Alpha 1");

        boolean shouldContinue = command.execute(context);

        assertTrue(shouldContinue);
        Task task = project.getProjectTasks().get(0);
        assertTrue(task.isDone());
        assertEquals("marked", ui.getLastAction());
        assertSame(task, ui.getLastTask());
        assertTrue(Boolean.TRUE.equals(ui.getLastMarkedState()));
    }

    @Test
    void unmarkCommandMarksTaskAsNotDone() throws Exception {
        project.addTask("Initial task");
        project.getProjectTasks().mark(0);
        UnmarkCommand command = new UnmarkCommand("Alpha 1");

        boolean shouldContinue = command.execute(context);

        assertTrue(shouldContinue);
        Task task = project.getProjectTasks().get(0);
        assertFalse(task.isDone());
        assertEquals("marked", ui.getLastAction());
        assertSame(task, ui.getLastTask());
        assertTrue(Boolean.FALSE.equals(ui.getLastMarkedState()));
    }

    @Test
    void deleteCommandRemovesTask() throws Exception {
        project.addTask("Task to delete");
        Task taskToDelete = project.getProjectTasks().get(0);
        DeleteCommand command = new DeleteCommand("Alpha 1");

        boolean shouldContinue = command.execute(context);

        assertTrue(shouldContinue);
        assertEquals(0, project.size());
        assertEquals("deleted", ui.getLastAction());
        assertSame(taskToDelete, ui.getLastTask());
        assertSame(project, ui.getLastProject());
    }

    @Test
    void updateCommandUpdatesTaskAttributes() throws Exception {
        project.addTask("Initial task", LocalDate.of(2024, 1, 1), 1);
        UpdateCommand command = new UpdateCommand(
                "Alpha 1 --description Updated task --deadline 2024-12-31 --priority high");

        boolean shouldContinue = command.execute(context);

        assertTrue(shouldContinue);
        Task updatedTask = project.getProjectTasks().get(0);
        assertEquals("Updated task", updatedTask.getDescription());
        assertEquals(LocalDate.of(2024, 12, 31), updatedTask.getDeadline());
        assertEquals(3, updatedTask.getPriority());
        assertEquals("updated", ui.getLastAction());
        assertSame(updatedTask, ui.getLastTask());
        assertSame(project, ui.getLastProject());
    }

    private static class StubConsoleUi extends ConsoleUi {
        private String lastAction;
        private Project lastProject;
        private Task lastTask;
        private Boolean lastMarkedState;

        StubConsoleUi(ProjectList projects) {
            super(projects);
        }

        @Override
        public void showAddedTask(Project targetProject) {
            lastAction = "added";
            lastProject = targetProject;
            if (targetProject.getProjectTasks().size() > 0) {
                lastTask = targetProject.getProjectTasks().get(targetProject.getProjectTasks().size() - 1);
            }
        }

        @Override
        public void showMarked(String projectName, Task t, boolean nowDone) {
            lastAction = "marked";
            lastMarkedState = nowDone;
            lastTask = t;
        }

        @Override
        public void showDeletedTask(Project targetProject, Task deletedTask) {
            lastAction = "deleted";
            lastProject = targetProject;
            lastTask = deletedTask;
        }

        @Override
        public void showUpdatedTask(Project targetProject, Task updatedTask) {
            lastAction = "updated";
            lastProject = targetProject;
            lastTask = updatedTask;
        }

        String getLastAction() {
            return lastAction;
        }

        Project getLastProject() {
            return lastProject;
        }

        Task getLastTask() {
            return lastTask;
        }

        Boolean getLastMarkedState() {
            return lastMarkedState;
        }
    }
}
