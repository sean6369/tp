package seedu.flowcli.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectList {
    private final List<Project> projects = new ArrayList<>();

    public void addProject(String projectName) {
        projects.add(new Project(projectName));
    }

    public Project delete(int zeroBasedIndex) {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= projects.size()) {
            throw new IndexOutOfBoundsException("Project index " + (zeroBasedIndex + 1) +
                    " out of range. Valid range: 1-" + projects.size());
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

    public Project getProjectByIndex(int zeroBasedIndex) {
        if (zeroBasedIndex < 0 || zeroBasedIndex >= projects.size()) {
            throw new IndexOutOfBoundsException("Project index " + (zeroBasedIndex + 1) +
                    " out of range. Valid range: 1-" + projects.size());
        }
        return projects.get(zeroBasedIndex);
    }

    public List<Project> getProjectList() {
        return projects;
    }

    public int getProjectListSize() {
        return projects.size();
    }

    public Project getProject(String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }

        return null;
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
