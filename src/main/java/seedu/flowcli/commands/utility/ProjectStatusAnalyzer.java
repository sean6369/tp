package seedu.flowcli.commands.utility;

import seedu.flowcli.project.Project;

/**
 * Utility class for analyzing project completion status.
 * Provides data about task completion without handling presentation.
 */
public class ProjectStatusAnalyzer {

    /**
     * Represents the status data for a project.
     */
    public static class ProjectStatus {
        private final String projectName;
        private final int totalTasks;
        private final int completedTasks;
        private final double percentage;

        public ProjectStatus(String projectName, int totalTasks, int completedTasks) {
            this.projectName = projectName;
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
            this.percentage = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;
        }

        public String getProjectName() {
            return projectName;
        }

        public int getTotalTasks() {
            return totalTasks;
        }

        public int getCompletedTasks() {
            return completedTasks;
        }

        public double getPercentage() {
            return percentage;
        }

        public boolean hasNoTasks() {
            return totalTasks == 0;
        }
    }

    /**
     * Analyzes a project and calculates its completion status.
     *
     * @param project The project to analyze
     * @return ProjectStatus object containing completion data
     */
    public static ProjectStatus analyzeProject(Project project) {
        int totalTasks = project.size();
        int completedTasks = 0;

        for (int i = 0; i < totalTasks; i++) {
            if (project.getProjectTasks().get(i).isDone()) {
                completedTasks++;
            }
        }

        return new ProjectStatus(project.getProjectName(), totalTasks, completedTasks);
    }
}
