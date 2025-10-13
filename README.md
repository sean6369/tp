# FlowCLI

FlowCLI is a fast, minimal CLI task manager for organizing projects and tasks.

![FlowCLI Mascot](docs/images/jojo.png)

## Features

FlowCLI allows you to manage projects and tasks via command line. Below are all available commands with examples.

### Commands

#### Project Management
- **Add Project**: `add <name>`
  - Input: `add work`
  - Expected Output: Confirmation of project added.

- **List Projects**: `list`
  - Input: `list`
  - Expected Output: Lists all projects with indices and clears filter/sort state.

- **List Tasks in Project**: `list <project>`
  - Input: `list work`
  - Expected Output: Lists all tasks in the "work" project.

- **Delete Project**: `delete project <name>`
  - Input: `delete project work`
  - Expected Output: Confirmation of project deleted.

#### Task Management
- **Add Task**: `add <project> <description> [--priority high/medium/low] [--deadline YYYY-MM-DD]`
  - Input: `add work finish report --priority high --deadline 2025-12-31`
  - Expected Output: Confirmation of task added with priority and deadline.
  - Input: `add work simple task`
  - Expected Output: Task added with default medium priority and no deadline.

- **Mark Task Done**: `mark <project> <index>`
  - Input: `mark work 1`
  - Expected Output: Task 1 in "work" marked as done ([X]).

- **Unmark Task**: `unmark <project> <index>`
  - Input: `unmark work 1`
  - Expected Output: Task 1 in "work" unmarked ([ ]).

- **Delete Task**: `delete task <project> <index>`
  - Input: `delete task work 1`
  - Expected Output: Confirmation of task deleted.

#### Global Sorting and Filtering
- **Sort Tasks by Deadline**: `sort tasks by deadline ascending` or `sort tasks by deadline descending`
  - Input: `sort tasks by deadline ascending`
  - Expected Output: All tasks sorted by deadline (ascending), showing project context (e.g., "work: finish report").

- **Sort Tasks by Priority**: `sort tasks by priority ascending` or `sort tasks by priority descending`
  - Input: `sort tasks by priority descending`
  - Expected Output: All tasks sorted by priority (high to low), showing project context.

- **Filter Tasks by Priority**: `filter tasks by priority <value>`
  - Input: `filter tasks by priority high`
  - Expected Output: Only tasks with high priority, showing project context.

- **Filter Tasks by Project**: `filter tasks by project <name>`
  - Input: `filter tasks by project work`
  - Expected Output: Only tasks from project "work", showing project context.

#### Export Tasks
- **Export Tasks to TXT File**: `export tasks to <filename> [<project>] [filter by <type> <value>] [sort by <field> <order>] [--all]`
  - Input: `export tasks to my_tasks.txt`
  - Expected Output: Exports last displayed view (if any) or all tasks if no view exists to my_tasks.txt file.
  - Input: `export tasks to all_tasks.txt --all`
  - Expected Output: Always exports all tasks from all projects to all_tasks.txt file (ignores last view).
  - Input: `export tasks to work_tasks.txt work`
  - Expected Output: Exports all tasks from "work" project to work_tasks.txt file.
  - Input: `export tasks to high_priority.txt filter by priority high`
  - Expected Output: Exports all high priority tasks to high_priority.txt file.
  - Input: `export tasks to sorted_tasks.txt sort by deadline ascending`
  - Expected Output: Exports all tasks sorted by deadline (ascending) to sorted_tasks.txt file.
  - Input: `export tasks to filtered_sorted.txt work filter by priority high sort by deadline ascending`
  - Expected Output: Exports high priority tasks from "work" project, sorted by deadline, to filtered_sorted.txt file.

- **Clear View State**: `list`
  - Input: `list`
  - Expected Output: Shows all projects and clears filter/sort state.

#### Other
- **Help**: `help`
  - Input: `help`
  - Expected Output: Displays all available commands.

- **Exit**: `bye`
  - Input: `bye`
  - Expected Output: Exits the application with goodbye message.

## Setting up in Intellij

Prerequisites: JDK 17 (use the exact version), update Intellij to the most recent version.

1. **Ensure Intellij JDK 17 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 17 in a previous Intellij project.
1. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
1. **Verify the setup**: After the importing is complete, locate the `src/main/java/seedu/flowcli/FlowCLI.java` file, right-click it, and choose `Run FlowCLI.main()`. If the setup is correct, you should see the welcome message and command prompt.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Build automation using Gradle

* This project uses Gradle for build automation and dependency management. It includes a basic build script as well (i.e. the `build.gradle` file).
* If you are new to Gradle, refer to the [Gradle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/gradle.html).

## Testing

### I/O redirection tests

* To run _I/O redirection_ tests (aka _Text UI tests_), navigate to the `text-ui-test` and run the `runtest(.bat/.sh)` script.

### JUnit tests

* Unit tests are provided in `src/test/java/seedu/flowcli/`.
* Run tests with `./gradlew test`.
* If you are new to JUnit, refer to the [JUnit Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/junit.html).

## Checkstyle

* A sample CheckStyle rule configuration is provided in this project.
* If you are new to Checkstyle, refer to the [Checkstyle Tutorial at se-education.org/guides](https://se-education.org/guides/tutorials/checkstyle.html).

## CI using GitHub Actions

The project uses [GitHub actions](https://github.com/features/actions) for CI. When you push a commit to this repo or PR against it, GitHub actions will run automatically to build and verify the code as updated by the commit/PR.

## Documentation

`/docs` folder contains a skeleton version of the project documentation.

Steps for publishing documentation to the public:
1. If you are using this project template for an individual project, go your fork on GitHub.<br>
   If you are using this project template for a team project, go to the team fork on GitHub.
1. Click on the `settings` tab.
1. Scroll down to the `GitHub Pages` section.
1. Set the `source` as `master branch /docs folder`.
1. Optionally, use the `choose a theme` button to choose a theme for your documentation.
