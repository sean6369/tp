package seedu.flowcli.project;

import java.util.ArrayList;
import java.util.List;

import seedu.flowcli.exceptions.IndexOutOfRangeException;
import seedu.flowcli.exceptions.ProjectNotFoundException;

//@@author Zhenzha0
public class ProjectList {
    private final List<Project> projects = new ArrayList<>();

    public void addProject(String projectName) {
        projects.add(new Project(projectName));
    }

    public Project delete(int zeroBasedIndex) throws IndexOutOfRangeException {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= projects.size()) {
            throw new IndexOutOfRangeException(projects.size());
        }
        Project returnProject = projects.get(zeroBasedIndex);
        projects.remove(zeroBasedIndex);
        return returnProject;
    }

    public Project deleteProject(Project project) {
        Project removedProject = project;
        projects.remove(project);
        return removedProject;
    }

    public Project getProjectByIndex(int zeroBasedIndex) throws IndexOutOfRangeException {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= projects.size()) {
            throw new IndexOutOfRangeException(projects.size());
        }
        return projects.get(zeroBasedIndex);
    }

    public List<Project> getProjectList() {
        return projects;
    }

    public int getProjectListSize() {
        return projects.size();
    }

    public Project getProject(String projectName) throws ProjectNotFoundException {
        for (Project project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }

        throw new ProjectNotFoundException(projectName);
    }

    public boolean isEmpty() {
        return projects.isEmpty();
    }

    public String render() {
        String output = "";
        for (Project project : projects) {
            output += project;
        }

        return output;
    }

}
//@@author
