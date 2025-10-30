package seedu.flowcli.project;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.ProjectNotFoundException;

@DisplayName("ProjectList Unit Tests")
class ProjectListTest {

    private ProjectList projectList;

    @BeforeEach
    void setUp() {
        projectList = new ProjectList();
    }

    @Test
    @DisplayName("constructor_createsEmptyProjectList")
    void constructorCreatesEmpty() {
        ProjectList pl = new ProjectList();

        assertTrue(pl.isEmpty(), "New project list should be empty");
        assertEquals(0, pl.getProjectListSize(), "Size should be 0");
    }

    @Test
    @DisplayName("addProject_addsProjectToList")
    void addProjectAdds() {
        projectList.addProject("Project1");

        assertAll("Add project",
                () -> assertEquals(1, projectList.getProjectListSize()),
                () -> assertFalse(projectList.isEmpty()),
                () -> assertEquals("Project1", projectList.getProjectByIndex(0).getProjectName())
        );
    }

    @Test
    @DisplayName("addProject_multipleProjects_increasesSize")
    void addMultipleProjects() {
        projectList.addProject("Project1");
        projectList.addProject("Project2");
        projectList.addProject("Project3");

        assertEquals(3, projectList.getProjectListSize());
        assertFalse(projectList.isEmpty());
    }

    @Test
    @DisplayName("delete_validIndex_removesAndReturnsProject")
    void deleteValidIndex() throws Exception {
        projectList.addProject("Project1");
        projectList.addProject("Project2");

        Project deletedProject = projectList.delete(0);

        assertAll("Delete project",
                () -> assertEquals("Project1", deletedProject.getProjectName()),
                () -> assertEquals(1, projectList.getProjectListSize()),
                () -> assertEquals("Project2", projectList.getProjectByIndex(0).getProjectName())
        );
    }

    @Test
    @DisplayName("delete_invalidIndex_throwsIndexOutOfRangeException")
    void deleteInvalidIndex() {
        projectList.addProject("Project1");

        assertThrows(IndexOutOfRangeException.class, () -> projectList.delete(5),
                "Should throw when index is out of range");
    }

    @Test
    @DisplayName("delete_negativeIndex_throwsIndexOutOfRangeException")
    void deleteNegativeIndex() {
        projectList.addProject("Project1");

        assertThrows(IndexOutOfRangeException.class, () -> projectList.delete(-1),
                "Should throw when index is negative");
    }

    @Test
    @DisplayName("deleteProject_byReference_removesProject")
    void deleteProjectByReference() throws Exception {
        projectList.addProject("Project1");
        projectList.addProject("Project2");
        Project projectToDelete = projectList.getProjectByIndex(0);

        Project deletedProject = projectList.deleteProject(projectToDelete);

        assertAll("Delete by reference",
                () -> assertEquals("Project1", deletedProject.getProjectName()),
                () -> assertEquals(1, projectList.getProjectListSize())
        );
    }

    @Test
    @DisplayName("getProjectByIndex_validIndex_returnsProject")
    void getProjectByIndexValid() throws Exception {
        projectList.addProject("Project1");
        projectList.addProject("Project2");

        Project project1 = projectList.getProjectByIndex(0);
        Project project2 = projectList.getProjectByIndex(1);

        assertEquals("Project1", project1.getProjectName());
        assertEquals("Project2", project2.getProjectName());
    }

    @Test
    @DisplayName("getProjectByIndex_invalidIndex_throwsIndexOutOfRangeException")
    void getProjectByIndexInvalid() {
        projectList.addProject("Project1");

        assertThrows(IndexOutOfRangeException.class, () -> projectList.getProjectByIndex(5),
                "Should throw when index is out of range");
    }

    @Test
    @DisplayName("getProject_validName_returnsProject")
    void getProjectValidName() throws Exception {
        projectList.addProject("Project1");
        projectList.addProject("Project2");

        Project project = projectList.getProject("Project1");

        assertEquals("Project1", project.getProjectName());
    }

    @Test
    @DisplayName("getProject_caseInsensitive_returnsProject")
    void getProjectCaseInsensitive() throws Exception {
        projectList.addProject("Project1");

        Project projectLower = projectList.getProject("project1");
        Project projectUpper = projectList.getProject("PROJECT1");

        assertEquals("Project1", projectLower.getProjectName());
        assertEquals("Project1", projectUpper.getProjectName());
    }

    @Test
    @DisplayName("getProject_nonExistentName_throwsProjectNotFoundException")
    void getProjectNonExistent() {
        projectList.addProject("Project1");

        assertThrows(ProjectNotFoundException.class,
                () -> projectList.getProject("NonExistent"),
                "Should throw when project doesn't exist");
    }

    @Test
    @DisplayName("getProjectList_returnsInternalList")
    void getProjectListReturnsInternal() {
        projectList.addProject("Project1");
        projectList.addProject("Project2");

        assertNotNull(projectList.getProjectList());
        assertEquals(2, projectList.getProjectList().size());
    }

    @Test
    @DisplayName("getProjectListSize_correctlyTracksSize")
    void getProjectListSizeTracksCorrectly() throws Exception {
        assertEquals(0, projectList.getProjectListSize());

        projectList.addProject("Project1");
        assertEquals(1, projectList.getProjectListSize());

        projectList.addProject("Project2");
        assertEquals(2, projectList.getProjectListSize());

        projectList.delete(0);
        assertEquals(1, projectList.getProjectListSize());
    }

    @Test
    @DisplayName("isEmpty_correctlyReflectsState")
    void isEmptyReflectsState() throws Exception {
        assertTrue(projectList.isEmpty());

        projectList.addProject("Project1");
        assertFalse(projectList.isEmpty());

        projectList.delete(0);
        assertTrue(projectList.isEmpty());
    }

    @Test
    @DisplayName("render_formatsProjectsCorrectly")
    void renderFormatsCorrectly() throws Exception {
        projectList.addProject("Project1");
        projectList.getProjectByIndex(0).addTask("Task 1");
        projectList.addProject("Project2");
        projectList.getProjectByIndex(1).addTask("Task 2");

        String rendered = projectList.render();

        assertTrue(rendered.contains("Project1"));
        assertTrue(rendered.contains("Project2"));
        assertTrue(rendered.contains("Task 1"));
        assertTrue(rendered.contains("Task 2"));
    }

    @Test
    @DisplayName("render_emptyList_returnsEmptyString")
    void renderEmptyList() {
        String rendered = projectList.render();

        assertEquals("", rendered);
    }

    @Test
    @DisplayName("addProject_withDuplicateNames_allowsDuplicates")
    void addProjectDuplicateNames() throws Exception {
        projectList.addProject("Project1");
        projectList.addProject("Project1");

        assertEquals(2, projectList.getProjectListSize());
    }

    @Test
    @DisplayName("deleteProject_nonExistentProject_doesNotThrow")
    void deleteProjectNonExistent() {
        projectList.addProject("Project1");
        Project differentProject = new Project("Different");

        // Should not throw exception
        projectList.deleteProject(differentProject);
        assertEquals(1, projectList.getProjectListSize(), "Original project should still exist");
    }

    @Test
    @DisplayName("multipleOperations_maintainsConsistency")
    void multipleOperationsMaintainConsistency() throws Exception {
        projectList.addProject("Project1");
        projectList.addProject("Project2");
        projectList.addProject("Project3");
        assertEquals(3, projectList.getProjectListSize());

        projectList.delete(1); // Remove Project2
        assertEquals(2, projectList.getProjectListSize());
        assertEquals("Project1", projectList.getProjectByIndex(0).getProjectName());
        assertEquals("Project3", projectList.getProjectByIndex(1).getProjectName());

        projectList.addProject("Project4");
        assertEquals(3, projectList.getProjectListSize());

        Project retrieved = projectList.getProject("project1");
        assertEquals("Project1", retrieved.getProjectName());
    }
}

