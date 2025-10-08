package Filter;
import task.Task;
import task.TaskList;
import project.Project;
import project.ProjectList;

/**
 * Searches for tasks whose descriptions contain a specified search string.
 * The search is case-insensitive.
 */
public class ProjectFilter {
    private ProjectList projects;
    private String searchString;
    private ProjectList matchingProjects;

    public ProjectList getMatchingTasks() {
        return matchingProjects;
    }

    public ProjectFilter(ProjectList projects, String searchString){
        this.projects = projects;
        this.searchString = searchString;
        search();
    }

    public void search() {
        matchingProjects = new ProjectList();
        for(Project project : projects.getProjectList()){
            if(project.getProjectName().toLowerCase().contains(searchString.toLowerCase())){
                matchingProjects.getProjectList().add(project);
            }
        }
    }

}

