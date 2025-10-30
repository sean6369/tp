![FlowCLI Cover](images/logos/FlowCLI-Cover-Image.png)

# Developer Guide

---

## Acknowledgements

This project is based on AddressBook-Level3 (AB3) from the [SE-EDU initiative](https://se-education.org).
We adapted its architecture, parser logic, and command execution framework.

We also acknowledge:

- [PlantUML](https://plantuml.com) for UML diagram generation.
- NUS CS2113 teaching team for guidance and templates.

---

## Table of Contents

- [Architecture](#architecture-yao-xiang)
- [Implementation &amp; Design](#implementation--design)
  - [Core Functionality](#core-functionality)
    - [Command Processing Infrastructure](#command-processing-infrastructure-by-zhenzhaoteamzhenzhamd)
    - [Task Management Features](#task-management-features-by-zing-jenteamzingjenmd)
    - [Project Management Features](#project-management-features-by-xylon-chanteamxylonchanmd)
    - [Common Classes](#common-classes-by-zhenzhaoteamzhenzhamd)
  - [Data Processing](#data-processing)
    - [Task Sorting Algorithm](#task-sorting-algorithm-yao-xiang)
    - [Task Filtering Algorithm](#task-filtering-algorithm-yao-xiang)
  - [Data Persistence](#data-persistence)
  - [User Interface](#user-interface)
    - [Interactive Mode](#interactive-mode-yao-xiang)
    - [Status Display System](#status-display-system-by-zhenzhaoteamzhenzhamd)
    - [Interactive Command Flows](#interactive-command-flows-yao-xiang)
- [Product scope](#product-scope)
- [User Stories](#user-stories)
- [Non-Functional Requirements](#non-functional-requirements-zhenzhao)
- [Glossary](#glossary)
- [Instructions for manual testing](#instructions-for-manual-testing)

---

## Architecture by [Yao Xiang](team/yxiang-828.md)

FlowCLI follows a layered architecture with clear separation of concerns:

![Architecture Diagram](plantUML/architecture/architecture.png)

**Key Design Principles:**

- **Single Responsibility**: Each class has one primary responsibility
- **Dependency Injection**: Components receive dependencies rather than creating them
- **Command Pattern**: All operations implemented as command objects
- **Layered Architecture**: UI → Logic → Model separation

---

## Implementation & Design

### Core Functionality

#### Command Processing Infrastructure by [Zhenzhao](team/zhenzhao.md)

The command processing infrastructure forms the foundation of FlowCLI, handling all user input parsing, validation, and command execution. It consists of three key components that work together to transform user input into executable commands.

**Key Components:**

- **CommandHandler** - Manages the main command loop, reads user input, coordinates parsing and execution
- **CommandParser** - Parses command words and extracts arguments, maps input to CommandType enum
- **ArgumentParser** - Parses project identifiers (index or name), resolves to Project objects, validates existence

![Command Processing Infrastructure Sequence](plantUML/command-processing-infrastructure/Command-processing-infrastructure.png)

**Implementation Details:**

1. **CommandHandler** initializes the application's main loop and scanner, reading user input line by line.
2. For each input line, **CommandParser** splits the command word from arguments and maps it to a CommandType enum.
3. The CommandFactory creates the appropriate Command object based on the type.
4. Before execution, **ArgumentParser** validates and resolves project identifiers (if applicable), converting indices or names into Project objects.
5. The Command executes with validated arguments and displays results through ConsoleUi.
6. Exceptions are caught and handled gracefully, displaying user-friendly error messages.

**Design Rationale:**

- **Separation of Concerns**: CommandParser handles syntax, ArgumentParser handles semantics, CommandHandler orchestrates the flow.
- **Reusability**: ArgumentParser is used by multiple commands that need project resolution.
- **Extensibility**: New commands can be added by extending the CommandType enum and Command base class.
- **Error Handling**: Validation happens early in the pipeline, preventing invalid state changes.

**Example Flow:**

```
User input: "add-task 1 Fix bug --priority high"
↓
CommandParser: type=ADD_TASK, args="1 Fix bug --priority high"
↓
ArgumentParser: projectIndex=1 → resolves to Project("CS2113T"), remaining="Fix bug --priority high"
↓
AddCommand: validates and adds task to project
↓
ConsoleUi: displays confirmation
```

---

#### Validation Framework by [Sean Lee](team/seanlee.md)

Input validation is centralized to ensure consistent rules, clear error messages, and early failure before any state mutation.

**Key Components:**

- `CommandValidator` - Reusable validation utilities used by parsers and commands
- `Validation` (constants) - Canonical limits, messages, and regex patterns

**Responsibilities:**

1. Normalize and validate primitive inputs (indices, names, flags, dates, priorities)
2. Guard command execution with preconditions (presence, range, format)
3. Provide consistent, user-friendly error messages across commands

**Common Rules (from `Validation`):**

- Index range: `INDEX_MIN = 1` (user-facing), non-negative zero-based internally
- Project name: `MAX_PROJECT_NAME_LENGTH`, `PROJECT_NAME_PATTERN`
- Task description: `MAX_DESCRIPTION_LENGTH`, non-blank
- Priority: allowed values {`high`, `medium`, `low`} (case-insensitive)
- Date format: `DATE_FORMAT = yyyy-MM-dd` with strict parsing

**Typical Usage:**

```java
// In ArgumentParser / Command classes
int projectIndex = CommandValidator.requireIndex(arg0, Validation.INDEX_MIN);
String name = CommandValidator.requireName(projectName, Validation.MAX_PROJECT_NAME_LENGTH, Validation.PROJECT_NAME_PATTERN);
LocalDate deadline = CommandValidator.optionalDateOrNull(deadlineArg, Validation.DATE_FORMAT);
int priority = CommandValidator.optionalPriorityOrDefault(priorityArg, Validation.ALLOWED_PRIORITIES, Validation.DEFAULT_PRIORITY);
```

**Error Handling:**

- Throws specific exceptions (e.g., `MissingArgumentException`, `InvalidFormatException`, `OutOfRangeException`)
- Messages are composed using `Validation.MESSAGE_*` templates for consistency
- All validation happens before model mutation to preserve integrity

**Integration Points:**

1. `CommandParser` uses validator for early syntax/flag checks
2. `ArgumentParser` uses validator to resolve and range-check indices and names
3. Individual commands validate optional fields (e.g., update flags, export filters) via the same utilities

**Parser hooks: index parsing and project index validation**

- `CommandParser.parseIndexOrNull(indexText, maxIndex)`
  - Parses a 1-based user index; converts to zero-based on success
  - Throws: `MissingIndexException`, `InvalidIndexFormatException`, `IndexOutOfRangeException`
  - Ensures indices respect `Validation.INDEX_MIN` and the provided upper bound

- `ArgumentParser.validateProjectIndex()`
  - Verifies that a target project is resolvable from user input
  - Detects non-numeric tokens and out-of-range indices against the current project list
  - Throws: `MissingArgumentException`, `InvalidIndexFormatException`, `IndexOutOfRangeException`, `InvalidArgumentException`

**Centralized error handling (refactored for consistency):**

- Specific exceptions replace a catch‑all error: `MissingArgumentException`, `MissingIndexException`, `InvalidIndexFormatException`, `IndexOutOfRangeException`, `InvalidDateException`, `InvalidFilenameException`, etc.
- Messages are standardized via `Validation.MESSAGE_*` templates and surfaced consistently by the command layer.
- Validation occurs before any model change; commands catch and render user-friendly messages, keeping parsing/validation logic separate from state mutation.

---

#### Task Management features by [Zing Jen](team/zingjen.md)

The task management system forms the core of FlowCLI, allowing users to create, track, and manage their work within different projects. The implementation follows the command pattern, where each user action is encapsulated in a dedicated command class.

##### `add-task` command

The `add-task` command allows users to add a new task to a specified project. Users can provide a task description, an optional deadline, and an optional priority.

**Command format**: `add-task <projectIndex> <description> [--priority <level>] [--deadline <YYYY-MM-DD>]`

The following sequence diagram illustrates the process of adding a task:

![AddTaskSequenceDiagram](plantUML/task-management/add-task-seq-diag.png)

**Implementation Details**:

1.  The `CommandParser` identifies the `add-task` command and creates an `AddCommand` object.
2.  `AddCommand#execute()` is called.
3.  The command parses the arguments to extract the project index, task description, deadline, and priority.
4.  It validates that the project exists and that a task description is provided.
5.  If validation passes, it retrieves the `Project` object and calls `project.addTask()` to create and add the new task.
6.  The `ConsoleUi` then displays a confirmation message to the user.

---

##### `delete-task` command

The `delete-task` command is used to remove a task from a project. It requires the project index and the 1-based index of the task to be deleted.

**Command format**: `delete-task <projectIndex> <taskIndex>`

The sequence diagram below shows the workflow:

![DeleteTaskSequenceDiagram](plantUML/task-management/del-task-seq-diag.png)

**Implementation Details**:

1.  The `CommandParser` creates a `DeleteTaskCommand` object.
2.  `DeleteTaskCommand#execute()` validates the presence of the project index and task index.
3.  It ensures the specified project exists and the task index is within the valid range.
4.  If valid, it calls `project.deleteTask()` to remove the task from the project's `TaskList`.
5.  A success message, including the details of the deleted task, is shown to the user.

---

##### `mark` and `unmark` commands

These commands allow users to change the completion status of a task.

- `mark`: Marks a task as done.
- `unmark`: Marks a task as not done.

**Command format**:

- `mark <projectIndex> <taskIndex>`
- `unmark <projectIndex> <taskIndex>`

The process is illustrated in the following diagram:

![MarkUnmarkTaskSequenceDiagram](plantUML/task-management/mark-unmark-task-seq-diag.png)

**Implementation Details**:

1.  `MarkCommand` or `UnmarkCommand` is instantiated by the parser.
2.  The `execute()` method validates the project and task index.
3.  It retrieves the `Task` object and calls its `mark()` or `unmark()` method.
4.  The command includes logic to prevent redundant operations (e.g., marking an already-marked task).
5.  The UI confirms that the task status has been updated.

---

##### `update-task` command

The `update-task` command modifies the attributes of an existing task, such as its description, deadline, or priority.

**Command format**: `update-task <projectIndex> <taskIndex> [--description <desc>] [--deadline <YYYY-MM-DD|none>] [--priority <level>]`

The update process is shown below:

![UpdateTaskSequenceDiagram](plantUML/task-management/update-task-seq-diag.png)

**Implementation Details**:

1.  The `UpdateCommand` is responsible for parsing the various optional flags that specify which fields to update.
2.  It validates the project and task index.
3.  It calls `project.updateTask()`, passing the new values. The `updateTask` method handles the logic of only changing the fields that were provided in the command.
4.  The UI displays the updated task details.

---

##### `list` command

The `list` command displays tasks. It can either list all tasks in all projects or list the tasks of a specific project.

**Command format**: `list --all` or `list <projectIndex>`

The diagram below illustrates the listing process:

![ListTasksSequenceDiagram](plantUML/task-management/list-task-seq-diag.png)

**Implementation Details**:

1.  The `ListCommand` checks if a project index was provided.
2.  If a project index is given, it finds the project and calls `ConsoleUi` to display only the tasks for that project.
3.  If no project name is given, it iterates through the entire `ProjectList` and instructs the `ConsoleUi` to display all projects and their associated tasks.

#### Project Management features by [Xylon Chan](team/xylonchan.md)

##### CreateCommand

The `Create-project` command is facilitated by `ProjectList`and it is accessed by `CommandContext`. It extends `Command` with the feature of reading the user's project name input and creating a project entity.
Additionally , it implements the following operations:

- `ProjectList#getProject(String name)` - returns the project if it exists, or `null` if not found.
- `ProjectList#addProject(String name)` - adds a new project with the given name.
- `ConsoleUi#showAddedProject()` - notifies the user after successful creation.
- `CommandContext#getProjects()` - returns all the projects currently in the ProjectList

Given below is an example usage scenario and how the `create-project` feature behaves at each step

**User Input**: The user enters the `create-project` command with the project name (e.g., `create-project Alpha`).

**Parsing**: The CommandParser identifies the command as `create-project` and constructs a CreateCommand with the raw arguments.

**Execution**: The FlowCLI main loop invokes CreateCommand#execute(CommandContext). (Note: CreateCommand extends Command.)

**Argument Parsing**: Inside execute, CreateCommand extracts the project name from the arguments

**Validation**: The command validates that the name is non-blank; if blank, it throws `MissingArgumentException`. It then checks duplicates via `context.getProjects().getProject(name)` if present, it throws `ProjectAlreadyExistsException`.

**Creating the Project**: On success, the command calls `context.getProjects().addProject(name)` to persist the new project in the model.

**UI Feedback**: The command obtains the UI via context.getUi() and calls showAddedProject() (or the equivalent success method) to confirm creation to the user.

**Return/Logging**: The command returns true to signal success and logs at info/fine levels; failures log at warning and do not mutate the model.

Here is a sequence diagram illustrating the process:

![CreateCommandSequenceDiagram](plantUML/project-management/CreateCommandDiagram.png)

#### Common Classes by [Zhenzhao](team/zhenzhao.md)

The core data model of FlowCLI consists of four fundamental classes that represent the domain entities and their relationships. These classes form the foundation upon which all features are built.

##### Project, ProjectList, Task, and TaskList classes

![ProjectRelationshipDiagram](plantUML/project-management/ProjectClassDiagram.png)

**Class Relationships:**
- **ProjectList** contains multiple **Project** instances
- Each **Project** contains a **TaskList**
- Each **TaskList** contains multiple **Task** instances

This hierarchical structure allows for organized task management within distinct projects, with clear ownership and encapsulation of responsibilities.

#### Project class

##### Overview

Represents a single project and encapsulates its name and task collection `TaskList`. Allows for adding/updatig/deleting tasks within a project without directly coordinating multiple lower-level classes.

##### Requirements

`projectName` is non null and should be non-blank when constructed
`projectTasks` is non null after construction

##### Helping classes

- `TaskList` and `Task` (for managing per-project tasks).

-`ProjectList (container)` creates and returns Project instances.

##### API

- `Project(String projectName)` — Constructor that constructs an empty project with the given name.

- `String getProjectName()` — returns the name of the project.

- `TaskList getProjectTasks()` — returns the tasks in that project

- `void addTask(String description)` — adds a task.

- `void addTask(String description, LocalDate deadline, int priority)` — add a task with deadline and priority

- `Task deleteTask(int index) — remove and return the task at index.`

- `Task updateTask(int index, String newDescription, boolean updateDescription, LocalDate newDeadline, boolean updateDeadline, Integer newPriority, boolean updatePriority)` — Updates the task description , deadline and priority

- `String showAllTasks()` — render the project’s tasks to a printable string (delegates to `TaskList.render()`).

- `String toString()` — printable representation of the project header + rendered tasks.

#### ProjectList class

##### Overview

An ArrayList container of Project instances offering indexed access, name-lookup, and simple rendering. This is the central point for commands to manipuate the collection of projects (e.g., create-project, delete-project, list-projects).

##### Requirements

`projects` is non null after construction

##### Helping classes

- `Project` - element sotred in the list.

##### API

- `void addProject(String projectName)` — appends a new Project.

- `Project delete(int zeroBasedIndex)` — delete by index, return the removed Project for confirmation.

- `Project deleteProject(Project project)` — remove by identity and returns the removed project

- `Project getProjectByIndex(int zeroBasedIndex)` — indexed accessor.

- `List<Project> getProjectList()` — list the projects by name currently in the list

- `int getProjectListSize()` — returns the number of projects.

- `Project getProject(String projectName)` — returns the project via name-based lookup

- `String render()` — concatenate each project’s toString() into a printable block.

### Data Processing

#### Task Sorting Algorithm by [Yao Xiang](team/yxiang-828.md)

The sorting algorithm supports sorting tasks by deadline or priority in ascending/descending order:

![Task Sorting Algorithm](plantUML/task-sortiing-algo/task-sorting-algo.png)

**Algorithm Details:**

- **Time Complexity**: O(n log n) using Java's built-in sort
- **Space Complexity**: O(n) for task list copy
- **Deadline Handling**: Tasks without deadlines are sorted last in ascending order
- **Priority Mapping**: High(1) > Medium(2) > Low(3)

#### Task Filtering Algorithm by [Yao Xiang](team/yxiang-828.md)

The filtering algorithm supports filtering tasks by priority level and/or project name:

![Task Filtering Algorithm](plantUML/task-filtering-algo/task-filtering-algo.png)

**Algorithm Details:**

- **Time Complexity**: O(n) linear scan through all tasks
- **Space Complexity**: O(m) where m is number of matching tasks
- **Case Insensitive**: Project name and priority filtering ignore case
- **Multiple Filters**: Can combine priority and project name filters

### Data Persistence by [Sean Lee](team/seanlee.md)

#### Export Algorithm by [Sean Lee](team/seanlee.md)

The export algorithm supports saving project and task data to text files with filtering and sorting capabilities:

![Export Command State Diagram](plantUML/export-command/export-command-state-diagram.png)

**Key Classes:**
- `TaskExporter` - Handles file I/O operations and formatting
- `ExportCommandHandler` - Orchestrates export process and parameter parsing
- `TaskCollector` - Aggregates tasks from projects with project context
- `TaskWithProject` - Wrapper class enabling cross-project operations

**Export Process:**
1. Parse and validate export parameters (filename, project selection, filters, sorting)
2. Collect tasks based on specific project
3. Apply filtering and sorting if specified
4. Write tasks to file with proper formatting and error handling
5. Display success confirmation to user

**File Structure:**
```
Export Header
=============

ProjectName: [X] Task Description (Due: YYYY-MM-DD) [priority]
ProjectName: [ ] Another Task [priority]
```

**Error Handling:**
- Permission denied, directory not found, disk space issues
- File locked/in use, invalid filenames, read-only filesystem
- User-friendly error messages with actionable suggestions

### **User Interface**

### Interactive Mode by [Yao Xiang](team/yxiang-828.md)

![Interactive Mode Overview](plantUML/interactive-mode-overview/interactive-mode-overview.png)

#### Implementation Overview

The interactive mode transforms single-word commands into guided conversations. When a user types "add" without arguments, the system prompts for project selection, task details, and optional fields.

#### Command Processing Sequence by [Yao Xiang](team/yxiang-828.md)

The overall command processing workflow shows how user input flows through the system components:

![Command Processing Sequence Diagram](plantUML/command-processing-sequence/Command%20Processing%20Sequence%20Diagram.png)

**Architecture Flow**: User input → CommandHandler → InteractivePromptHandler (if needed) → CommandFactory → Command execution → Result display.

#### Class Diagram: InteractivePromptHandler Structure by [Yao Xiang](team/yxiang-828.md)

![InteractivePromptHandler Class Diagram](plantUML/interactive-prompt-handler/interactiveprompthandler.png)

#### Interactive Mode Detection by [Yao Xiang](team/yxiang-828.md)

The `CommandHandler.shouldUseInteractiveMode()` method determines when to trigger interactive mode:

```java
private boolean shouldUseInteractiveMode(CommandParser.ParsedCommand parsed) {
    switch (parsed.getType()) {
    case ADD_TASK:
        return parsed.getArguments().trim().isEmpty();
    case CREATE_PROJECT:
        return parsed.getArguments().trim().isEmpty();
    case LIST:
        return parsed.getArguments().trim().isEmpty();
    case MARK:
        return parsed.getArguments().trim().isEmpty();
    case UNMARK:
        return parsed.getArguments().trim().isEmpty();
    case DELETE:
        return parsed.getArguments().trim().isEmpty();
    case UPDATE:
        return parsed.getArguments().trim().isEmpty();
    case SORT:
        return parsed.getArguments().trim().isEmpty();
    case FILTER:
        return parsed.getArguments().trim().isEmpty();
    case EXPORT:
        return parsed.getArguments().trim().isEmpty();
    case STATUS:
        return parsed.getArguments().trim().isEmpty();
    default:
        return false;
    }
}
```

**Decision Rationale**: Interactive mode is triggered for main commands with empty arguments, preserving backward compatibility.

#### Status Display System by [Zhenzhao](team/zhenzhao.md)

The status display system provides users with visual feedback on project progress through completion tracking, progress bars, and motivational messages. It separates analysis logic from presentation concerns for maintainability.

##### Architecture Overview

![Status Display System Class Diagram](plantUML/status-display-system/status-display-class-diagram.png)

**Key Components:**

- **StatusCommand** - Entry point that parses arguments and coordinates status display
- **ProjectStatusAnalyzer** - Pure analysis logic that calculates completion statistics
- **ProjectStatus** - Immutable data transfer object holding project statistics
- **ConsoleUi** - Renders status information with progress bars and messages

**Design Principles:**

- **Separation of Concerns**: Analysis logic (ProjectStatusAnalyzer) is separate from UI rendering (ConsoleUi)
- **Data Transfer Objects**: ProjectStatus acts as an immutable container for statistics
- **Single Responsibility**: Each class has one clear purpose in the status display pipeline

##### Status Command Execution Flow

![Status Command Sequence Diagram](plantUML/status-command-sequence/Status-command-sequence-diagram.png)

**Implementation Details:**

1. **StatusCommand** receives arguments (project index or `--all` flag)
2. **ArgumentParser** validates and resolves project identifiers (if specific project)
3. **ProjectStatusAnalyzer** analyzes each project:
   - Iterates through tasks in the project's TaskList
   - Counts completed tasks (where `isDone() == true`)
   - Calculates completion percentage
   - Returns a ProjectStatus data object
4. **ConsoleUi** renders the status information:
   - Formats status summary (e.g., "3/5 tasks completed, 60%")
   - Generates visual progress bar: `[=========>      ] 60%`
   - Selects motivational message based on completion percentage
   - Displays formatted output to user

**Task Status Markers:**

Individual tasks display completion status using visual markers in list views:

```java
public String marker() {
    return isDone ? "[X]" : "[ ]";
}
```

**Display Example**:

```
CS2113T Project - Project Status
3/5 tasks completed, 60%
[========================>               ] 60%
We are on the right track, keep completing your tasks!
```

**Motivational Messages:**

The system provides contextual encouragement based on progress:
- ≤25%: "You are kinda cooked, start doing your tasks!"
- ≤50%: "You gotta lock in and finish all tasks!"
- ≤75%: "We are on the right track, keep completing your tasks!"
- >75%: "We are finishing all tasks!! Upzzz!"

**Status Types:**

1. **Single Project Status** (`status <projectIndex>`): Shows detailed status for one project
2. **All Projects Status** (`status --all`): Shows summary status for all projects in a compact format

#### Interactive Command Flows by [Yao Xiang](team/yxiang-828.md)

#### Add Command Flow by [Yao Xiang](team/yxiang-828.md)

The add command guides users through project selection, task description, priority, and optional deadline:

![Add Command Sequence Diagram](plantUML/add-command-sequence/Add%20Command%20Sequence%20Diagram.png)

**Key Features**:

- Project validation with range checking
- Required task description with empty string rejection
- Optional priority (defaults to "medium")
- Optional deadline with YYYY-MM-DD format validation

#### Add Command State Flow by [Yao Xiang](team/yxiang-828.md)

![Add Command State Diagram](plantUML/add-command-state/add-command-state.png)

**Optional Fields**: Priority and deadline can be skipped, defaulting to "medium" and no deadline respectively.

#### Mark/Unmark Command Flows by [Yao Xiang](team/yxiang-828.md)

Mark and unmark commands share similar logic but with different validation:

```java
public String handleMarkCommand() {
    Integer projectSelection = promptForProjectIndex();
    if (projectSelection == null) return null;

    // Display tasks with status markers
    System.out.println("Hmph, which tasks do you want to mark as done in " + projectName + ":");
    for (int i = 0; i < project.size(); i++) {
        var task = project.getProjectTasks().get(i);
        String status = task.isDone() ? "x" : " ";
        System.out.println((i + 1) + ". [" + status + "] " + task.getDescription());
    }

    // Multiple task selection with comma separation
    String input = scanner.nextLine().trim();
    String[] indices = input.split(",");
    // ... validation and construction
}
```

**Mark vs Unmark Validation**:

- **Mark**: Prevents marking already completed tasks
- **Unmark**: Prevents unmarking already incomplete tasks
- **Error Message**: "Your task is not even marked, what do you want me to unmark!"

#### Delete Command Flow by [Yao Xiang](team/yxiang-828.md)

Delete command uses a two-stage approach: type selection then specific item selection with confirmation:

![Delete Command State Diagram](plantUML/delete-command-state/delete-command-state.png)

**Safety Features**:

- Confirmation prompts for all destructive operations
- Clear project/task listing before selection
- Case-insensitive confirmation ("y", "yes", "n", "no")

#### Update Command Flow by [Yao Xiang](team/yxiang-828.md)

Update command implements recursive field updates allowing multiple changes in one session:

![Update Command State Diagram](plantUML/update-command-state/update-command-state.png)

**Recursive Design**: Users can update multiple fields without restarting the flow, with options to reselect tasks/projects or exit at any point.

#### Export Command Flow by [Yao Xiang](team/yxiang-828.md)

The export command provides comprehensive data export capabilities with multiple filtering and sorting options. Users can export their project and task data in various formats and configurations.

**Key Features:**

- **Multiple Export Types**: All tasks, specific projects, filtered tasks, sorted tasks, or combined filtered and sorted exports
- **Flexible Output**: Supports various file formats (typically CSV or JSON)
- **Data Filtering**: Can filter by project name, priority levels, or completion status
- **Data Sorting**: Can sort by deadline, priority, or other task attributes
- **File Naming**: Custom filename specification for organized data management

**Export Options:**

1. **All Tasks**: Exports every task across all projects
2. **Specific Project**: Exports only tasks from a selected project
3. **Filtered Tasks**: Exports tasks matching specific criteria (priority, project, status)
4. **Sorted Tasks**: Exports tasks in sorted order (by deadline, priority, etc.)
5. **Filtered and Sorted**: Combines filtering and sorting for precise data extraction

![Export Command State Diagram](plantUML/export-command/export-command-state-diagram.png)

#### Create Command Flow by [Yao Xiang](team/yxiang-828.md)

Create command prompts for a new project name with validation:

![Create Command State Diagram](plantUML/create-command-state/create-command-state.png)

**Validation**: Checks for empty names and duplicate project names.

#### Mark/Unmark Command Flows by [Yao Xiang](team/yxiang-828.md)

Mark and unmark commands follow identical selection flow with different validation:

![Mark/Unmark Command Sequence Diagram](plantUML/mark-unmark-sequence/Unmark%20Command%20Sequence%20Diagram.png)

**Shared Logic**: Both commands use identical project/task selection but different validation rules.

#### Sort Command Flow by [Yao Xiang](team/yxiang-828.md)

Sort command offers field and order selection:

![Sort Command State Diagram](plantUML/sort-command-state/sort-command-state.png)

**Simple Flow**: Two sequential choices with no complex validation.

#### Filter Command Flow by [Yao Xiang](team/yxiang-828.md)

Filter command offers priority level selection:

![Filter Command State Diagram](plantUML/filter-command-state/filter-command-state.png)

**Single Choice**: Simple selection from three priority options.

#### List Command Flow by [Yao Xiang](team/yxiang-828.md)

![List Command State Diagram](plantUML/list-command-state/list-command-state.png)

**Display Logic**: Shows numbered project list, then either displays tasks for selected project or all projects with all tasks.

#### Status Command Flow by [Yao Xiang](team/yxiang-828.md)

![Status Command State Diagram](plantUML/status-command-state/status-command-state.png)

**Status Types**: Shows either project-level statistics or task completion summaries.

## Product scope

---

### Target user profile

Individual student developers working on their own coursework, capstones, hackathons, or any other related projects that require task management to streamline their workflow.

---

### Value proposition

FlowCLI addresses the challenge of managing complex academic or personal projects by providing a streamlined, command-line interface for task and project organization. It helps student developers maintain focus, track progress, and efficiently handle multiple assignments or project phases without the overhead of graphical user interfaces. By offering quick task creation, flexible filtering, and clear status overviews, FlowCLI ensures that users can dedicate more time to coding and less to administrative overhead, ultimately boosting productivity and reducing stress.

---

## User Stories

| Version | As a ... | I want to ...                               | So that I can ...                                           |
| ------- | -------- | ------------------------------------------- | ----------------------------------------------------------- |
| v1.0    | new user | see usage instructions                      | refer to them when I forget how to use the application      |
| v1.0    | user     | find a to-do item by name                   | locate a to-do without having to go through the entire list |
| v1.0    | user     | create and manage projects and tasks        | organize my work and track progress efficiently             |
| v1.0    | user     | add, mark, unmark, delete, and update tasks | keep my task list accurate and up-to-date                   |
| v1.0    | user     | view, filter, and sort tasks                | focus on relevant tasks and prioritize my workload          |
| v1.0    | user     | export my tasks                             | backup my data or share it with others                      |
| v2.0    | user     | use interactive prompting for commands      | be guided through complex commands easily                   |
| v2.0    | user     | check the status of my projects and tasks   | get a quick overview of my progress and workload            |

---

## Non-Functional Requirements by [Zhenzhao](team/zhenzhao.md)

1. **Performance**
   - The application should respond to user commands within 500ms under normal operating conditions.
   - Loading and parsing project data should complete within 1 second for up to 100 projects with 1000 tasks total.
   - Sorting and filtering operations should complete within 200ms for typical datasets (up to 500 tasks).

2. **Usability**
   - The application should be usable by users with basic command-line knowledge without requiring extensive training.
   - Interactive mode prompts should guide users through command execution with clear, numbered options.
   - Error messages should be descriptive and suggest corrective actions where applicable.
   - All commands should have both short-form (for experienced users) and interactive mode (for new users).

3. **Reliability**
   - The application should handle invalid inputs gracefully without crashing.
   - All data validation should occur before any state changes to maintain data integrity.
   - Error handling should prevent data corruption in edge cases (e.g., concurrent file access, invalid date formats).

4. **Portability**
   - The application should run on any platform with Java 11 or higher installed (Windows, macOS, Linux).
   - No platform-specific dependencies should be required beyond the Java Runtime Environment.
   - File paths should use platform-independent representations where possible.

5. **Maintainability**
   - Code should follow standard Java coding conventions and style guidelines.
   - All public methods and classes should include Javadoc documentation.
   - The codebase should maintain clear separation of concerns between UI, logic, and model layers.
   - Each command should be implemented as a separate, testable class extending the Command base class.

6. **Scalability**
   - The application should handle at least 50 projects with 20 tasks each without performance degradation.
   - Memory usage should remain under 100MB for typical usage scenarios.

7. **Security**
   - User input should be validated and sanitized to prevent command injection or malicious input.
   - File operations should verify file paths to prevent unauthorized access to system files.

8. **Compatibility**
   - The application should be compatible with common terminal emulators (Command Prompt, PowerShell, Terminal, Bash).
   - Text output should be compatible with standard terminal character encoding (UTF-8).

---

## Glossary

- **CLI (Command-Line Interface)** - A text-based interface used to operate software and operating systems.
- **Task** - A unit of work within a project, often with a description, deadline, and priority.
- **Project** - A collection of related tasks, representing a larger initiative or goal.
- **Interactive Mode** - A mode of operation where the CLI guides the user through command inputs with prompts.
- **Command Pattern** - A behavioral design pattern in which an object is used to encapsulate all information needed to perform an action or trigger an event at a later time.
- **NFR (Non-Functional Requirement)** - Requirements that specify criteria that can be used to judge the operation of a system, rather than specific behaviors (e.g., performance, reliability, usability).

## Instructions for manual testing

These instructions will guide you through comprehensive manual testing of FlowCLI, including both inline command usage and interactive mode functionality.

### Prerequisites
- Ensure you have Java 17 or higher installed
- Ensure you have Gradle installed

### Setup Steps

1. **Build the application:**
   ```
   ./gradlew build
   ```

2. **Locate the JAR file:**
   - Navigate to `build\libs\`
   - Copy the full path of `flowcli.jar`

3. **Run the application:**
   ```
   java -jar <full-path-to-flowcli.jar>
   ```

### Sample Data Setup

4. **Load sample data:**
   Copy and paste the following commands to populate the application with sample data:
   (no need to copy paste one at a time)
   ```
   create-project "CS2113T Project"
   create-project "Internship Hunt"
   create-project "Household Chores"
   create-project "Fitness Plan"
   create-project "Side Project - Website"

   add-task 1 "Finalize DG" --priority high --deadline 2025-11-10
   add-task 1 "Implement UI" --priority high --deadline 2025-11-20
   add-task 1 "Write UG" --priority medium --deadline 2025-11-25
   add-task 1 "Prepare for Demo" --priority medium
   add-task 1 "Review teammate PR" --priority low

   add-task 2 "Update Resume" --priority high
   add-task 2 "Apply to 5 companies" --priority medium --deadline 2025-11-15
   add-task 2 "Research company A" --priority low
   add-task 2 "Practice LeetCode" --priority medium

   add-task 3 "Buy groceries" --priority medium --deadline 2025-10-29
   add-task 3 "Clean room" --priority low
   add-task 3 "Pay utility bill" --priority high --deadline 2025-11-01

   add-task 4 "Go for run" --priority medium
   add-task 4 "Meal prep for week" --priority low
   add-task 4 "Go to gym" --priority medium

   add-task 5 "Design homepage" --priority medium
   add-task 5 "Set up database" --priority high --deadline 2025-12-01
   add-task 5 "Draft 'About Me' page" --priority low

   mark 1 1
   mark 2 1
   mark 3 3
   mark 4 1
   ```

   Alternatively, you may create your own test data using the commands above as a reference.

### Testing Commands

5. **View help:**
   ```
   help
   ```

6. **Test inline command variations:**
   Follow the help output and test all inline command variations.


7. **Test interactive mode:**
   Follow the help output and try all one-word command triggers for interactive mode:
   - `add` (triggers interactive task addition)
   - `create` (triggers interactive project creation)
   - `list` (triggers interactive project/task listing)
   - `mark` (triggers interactive task marking)
   - `unmark` (triggers interactive task unmarking)
   - `update` (triggers interactive task updating)
   - `delete` (triggers interactive item deletion)
   - `sort` (triggers interactive sorting)
   - `filter` (triggers interactive filtering)
   - `export` (triggers interactive data export)

8. **Exit the application:**
   ```
   bye
   ```

### Expected Behavior

- **Inline commands**: Should execute immediately with provided arguments
- **Interactive mode**: Should prompt for additional information when commands are given without arguments
- **Error handling**: Should provide helpful error messages for invalid inputs
- **Help system**: Should provide comprehensive command reference

### Troubleshooting

- If the JAR file is not found, ensure the build completed successfully
- If commands fail, check that project/task indices exist
- If interactive mode doesn't trigger, ensure commands are entered without arguments
