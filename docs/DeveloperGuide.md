# Developer Guide

---

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

---

## Table of Contents

- [Architecture](#architecture-yao-xiang)
- [Implementation &amp; Design](#implementation--design)
  - [Core Functionality](#core-functionality)
  - [Data Processing](#data-processing)
  - [Data Persistence](#data-persistence)
  - [User Interface](#user-interface)
    - [Interactive Mode](#interactive-mode-yao-xiang)
- [Product scope](#product-scope)
- [User Stories](#user-stories)
- [Non-Functional Requirements](#non-functional-requirements)
- [Glossary](#glossary)
- [Instructions for manual testing](#instructions-for-manual-testing)

---

## Architecture (Yao Xiang)

FlowCLI follows a layered architecture with clear separation of concerns:

```plantuml
@startuml Architecture Diagram
!theme plain

package "UI Layer" {
    [ConsoleUi]
}

package "Logic Layer" {
    [CommandHandler]
    [InteractivePromptHandler]
    [CommandFactory]
    [Individual Command Classes]
}

package "Model Layer" {
    [ProjectList]
    [Project]
    [Task]
}

package "Utility Layer" {
    [Validation Classes]
    [Utility Classes]
    [TaskFilter/TaskSorter/etc.]
}

ConsoleUi --> CommandHandler
CommandHandler --> InteractivePromptHandler
CommandHandler --> CommandFactory
CommandFactory --> "Individual Command Classes"
"Individual Command Classes" --> ProjectList
"Individual Command Classes" --> "Task Operations"
ProjectList --> Project
Project --> Task
InteractivePromptHandler --> ProjectList
InteractivePromptHandler --> "Validation Classes"
"Individual Command Classes" --> "Utility Classes"
"Utility Classes" --> "TaskFilter/TaskSorter/etc."
@enduml
```

**Key Design Principles:**

- **Single Responsibility**: Each class has one primary responsibility
- **Dependency Injection**: Components receive dependencies rather than creating them
- **Command Pattern**: All operations implemented as command objects
- **Layered Architecture**: UI → Logic → Model separation

---

## Implementation & Design

### Core Functionality

Task Management: Add, update, and delete tasks.

Project Management: Create new projects.

### Data Processing

#### Task Sorting Algorithm (Yao Xiang)

The sorting algorithm supports sorting tasks by deadline or priority in ascending/descending order:

```plantuml
@startuml Task Sorting Algorithm
!theme plain

start
:TaskSorter constructor called
with (projects/tasks, sortBy, ascending);

if (inputTasks provided?) then (yes)
    :sortedTasks = copy of inputTasks;
else (no)
    :sortedTasks = collect all tasks from all projects;
endif

:Apply sorting algorithm;

if (sortBy == "deadline") then (yes)
    :Compare task deadlines;
    note right
        null deadlines sorted last
        in ascending order
    end note
else (sortBy == "priority")
    :Compare task priority levels;
    note right
        1=High, 2=Medium, 3=Low
    end note
endif

if (ascending?) then (no)
    :Reverse comparison result;
endif

:Return sorted task list;
stop
@enduml
```

**Algorithm Details:**

- **Time Complexity**: O(n log n) using Java's built-in sort
- **Space Complexity**: O(n) for task list copy
- **Deadline Handling**: Tasks without deadlines are sorted last in ascending order
- **Priority Mapping**: High(1) > Medium(2) > Low(3)

#### Task Filtering Algorithm (Yao Xiang)

The filtering algorithm supports filtering tasks by priority level and/or project name:

```plantuml
@startuml Task Filtering Algorithm
!theme plain

start
:TaskFilter constructor called
with (projects/tasks, priority, projectName);

:Initialize empty filteredTasks list;

if (inputTasks provided?) then (yes)
    :Process provided task list;
else (no)
    :Process all projects and their tasks;
endif

repeat
    :Get next task and project info;

    if (projectName filter set?) then (yes)
        if (task project != filter project) then (continue)
        endif
    endif

    if (priority filter set?) then (yes)
        :Convert task priority to string;
        if (task priority != filter priority) then (continue)
        endif
    endif

    :Add task to filtered results;
repeat while (more tasks?)

:Return filtered task list;
stop
@enduml
```

**Algorithm Details:**

- **Time Complexity**: O(n) linear scan through all tasks
- **Space Complexity**: O(m) where m is number of matching tasks
- **Case Insensitive**: Project name and priority filtering ignore case
- **Multiple Filters**: Can combine priority and project name filters

### Data Persistence

Export: Export current project and task data to a file.

### **User Interface**

### Interactive Mode (Yao Xiang)

```plantuml
@startuml Interactive Mode Overview
!theme plain

start
:User Input;
:CommandHandler.resolveCommand();

if (Should Use Interactive?) then (Yes)
    :InteractivePromptHandler;
    switch (Command Type)
    case (handleAddCommand)
        :handleAddCommand;
    case (handleCreateCommand)
        :handleCreateCommand;
    case (handleListCommand)
        :handleListCommand;
    case (handleMarkCommand)
        :handleMarkCommand;
    case (handleUnmarkCommand)
        :handleUnmarkCommand;
    case (handleDeleteCommand)
        :handleDeleteCommand;
    case (handleUpdateCommand)
        :handleUpdateCommand;
    case (handleSortCommand)
        :handleSortCommand;
    case (handleFilterCommand)
        :handleFilterCommand;
    case (handleExportCommand)
        :handleExportCommand;
    case (handleStatusCommand)
        :handleStatusCommand;
    endswitch
    :Return Constructed Args;
else (No)
    :Normal Command Parsing;
endif

:CommandFactory.create;
:Command.execute;
stop
@enduml
```

#### Implementation Overview

The interactive mode transforms single-word commands into guided conversations. When a user types "add" without arguments, the system prompts for project selection, task details, and optional fields.

#### Class Diagram: InteractivePromptHandler Structure (Yao Xiang)

```plantuml
@startuml InteractivePromptHandler Class Diagram
!theme plain

class InteractivePromptHandler {
    -ProjectList projects
    -Scanner scanner
    +InteractivePromptHandler(ProjectList, Scanner)
    +handleAddCommand(): String
    +handleCreateCommand(): String
    +handleListCommand(): String
    +handleMarkCommand(): String
    +handleUnmarkCommand(): String
    +handleDeleteCommand(): String
    +handleUpdateCommand(): String
    +handleSortCommand(): String
    +handleFilterCommand(): String
    +handleExportCommand(): String
    +handleStatusCommand(): String
    -promptForProjectIndex(): Integer
    -promptForPriority(): String
    -promptForDeadline(): String
    -promptForNewProjectName(): String
    -handleDeleteProject(): String
    -handleDeleteTask(): String
    -handleUpdateTaskInProject(int): String
    -handleUpdateTaskFields(int, int): String
    -promptForNewDescription(): String
    -promptForNewPriority(): String
    -promptForNewDeadline(): String
    -handleFilterByPriority(): String
}

class ProjectList {
    +getProjectListSize(): int
    +getProjectByIndex(int): Project
}

class Project {
    +getProjectName(): String
    +showAllTasks(): String
    +size(): int
}

class Task {
    -boolean isDone
    -String description
    -LocalDate deadline
    -int priority
    +marker(): String
    +toString(): String
    +mark(): void
    +unmark(): void
}

InteractivePromptHandler --> ProjectList : uses
InteractivePromptHandler --> Project : accesses
Project --> Task : contains
@enduml
```

#### Interactive Mode Detection (Yao Xiang)

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

#### Task Status Display System (Zhen Zhao)

Tasks display completion status using visual markers:

```java
public String marker() {
    return isDone ? "[X]" : "[ ]";
}

public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(marker()).append(" ").append(description);
    if (deadline != null) {
        sb.append(" (Due: ").append(deadline).append(")");
    }
    sb.append(" [").append(getPriorityString()).append("]");
    return sb.toString();
}
```

**Display Example**:

```
1. [X] Implement login feature (Due: Dec 31, 2024) [high]
2. [ ] Write unit tests [medium]
3. [ ] Fix bug in parser [low]
```

#### Interactive Command Flows (Yao Xiang)

#### Add Command Flow (Yao Xiang)

The add command guides users through project selection, task description, priority, and optional deadline:

```plantuml
@startuml Add Command Sequence Diagram
!theme plain

actor User as U
participant InteractivePromptHandler as IPH
participant ProjectList as PL

U -> IPH: handleAddCommand()
IPH -> PL: getProjectListSize()
IPH -> U: "Hmph, here are your projects:"
IPH -> U: Display numbered project list
IPH -> U: "Enter project number or press 'enter' to exit:"

U -> IPH: Project number
IPH -> IPH: Validate range
alt Invalid input
    IPH -> U: "Hmph, choose a project number within the range!"
    IPH -> U: Retry prompt
end

IPH -> U: "Enter task name:"
U -> IPH: Task description
IPH -> IPH: Validate non-empty
IPH -> U: Priority prompt (default: medium)
U -> IPH: Priority choice
IPH -> U: Deadline prompt (optional)
U -> IPH: Deadline or skip
IPH -> IPH: Construct command args
IPH -> U: Return to main flow

@enduml
```

**Key Features**:

- Project validation with range checking
- Required task description with empty string rejection
- Optional priority (defaults to "medium")
- Optional deadline with YYYY-MM-DD format validation

#### Add Command State Flow (Yao Xiang)

```plantuml
@startuml Add Command State Diagram
!theme plain

[*] --> ProjectSelection
ProjectSelection --> [*] : Cancelled
ProjectSelection --> DescriptionInput : Project chosen

DescriptionInput --> [*] : Cancelled/Empty
DescriptionInput --> PriorityPrompt : Description provided

PriorityPrompt --> DeadlinePrompt : Priority chosen/default
PriorityPrompt --> DeadlinePrompt : Skipped (default medium)

DeadlinePrompt --> [*] : Deadline provided (return args)
DeadlinePrompt --> [*] : Skipped (return args)
@enduml
```

**Optional Fields**: Priority and deadline can be skipped, defaulting to "medium" and no deadline respectively.

#### Mark/Unmark Command Flows (Yao Xiang)

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

#### Delete Command Flow (Yao Xiang)

Delete command uses a two-stage approach: type selection then specific item selection with confirmation:

```plantuml
@startuml Delete Command State Diagram
!theme plain

[*] --> TypeSelection
TypeSelection --> ProjectDelete : Choose "1. A project"
TypeSelection --> TaskDelete : Choose "2. A task"
TypeSelection --> [*] : Choose "3. Cancel"

ProjectDelete --> ProjectConfirmation

ProjectConfirmation --> [*] : Confirmed (return args)
ProjectConfirmation --> [*] : Cancelled (return null)

TaskDelete --> ProjectSelection
ProjectSelection --> TaskSelection
TaskSelection --> TaskConfirmation

TaskConfirmation --> [*] : Confirmed (return args)
TaskConfirmation --> [*] : Cancelled (return null)
@enduml
```

**Safety Features**:

- Confirmation prompts for all destructive operations
- Clear project/task listing before selection
- Case-insensitive confirmation ("y", "yes", "n", "no")

#### Update Command Flow (Yao Xiang)

Update command implements recursive field updates allowing multiple changes in one session:

```plantuml
@startuml Update Command State Diagram
!theme plain

[*] --> ProjectSelection
ProjectSelection --> [*] : Cancelled
ProjectSelection --> TaskSelection : Project chosen

TaskSelection --> [*] : Cancelled
TaskSelection --> FieldSelection : Task chosen

FieldSelection --> DescriptionUpdate : Choose "1. Description"
FieldSelection --> PriorityUpdate : Choose "2. Priority"
FieldSelection --> DeadlineUpdate : Choose "3. Deadline"
FieldSelection --> TaskSelection : Choose "4. Reselect task"
FieldSelection --> ProjectSelection : Choose "5. Reselect project"
FieldSelection --> [*] : Choose "6. Done"

DescriptionUpdate --> FieldSelection : Updated/Cancelled
PriorityUpdate --> FieldSelection : Updated/Cancelled
DeadlineUpdate --> FieldSelection : Updated/Cancelled
@enduml
```

**Recursive Design**: Users can update multiple fields without restarting the flow, with options to reselect tasks/projects or exit at any point.

#### Export Command Flow (Yao Xiang)

Export command offers multiple export options with filtering and sorting:

```plantuml
@startuml Export Command State Diagram
!theme plain

[*] --> FilenameInput
FilenameInput --> [*] : Cancelled
FilenameInput --> ExportTypeSelection : Filename provided

ExportTypeSelection --> [*] : Cancelled
ExportTypeSelection --> AllTasks : Choose "1. All tasks"
ExportTypeSelection --> SpecificProject : Choose "2. Specific project"
ExportTypeSelection --> FilteredTasks : Choose "3. Filtered tasks"
ExportTypeSelection --> SortedTasks : Choose "4. Sorted tasks"
ExportTypeSelection --> FilteredSorted : Choose "5. Filtered and sorted"

AllTasks --> Confirmation
SpecificProject --> Confirmation
FilteredTasks --> Confirmation
SortedTasks --> Confirmation
FilteredSorted --> Confirmation

Confirmation --> [*] : Confirmed (export file)
Confirmation --> [*] : Cancelled
@enduml
```

**Complex Options**: Supports all combinations of project selection, filtering, and sorting with final confirmation.

#### Create Command Flow (Yao Xiang)

Create command prompts for a new project name with validation:

```plantuml
@startuml Create Command State Diagram
!theme plain

[*] --> NameInput
NameInput --> [*] : Cancelled/Empty
NameInput --> Validation : Name provided

Validation --> [*] : Name exists (return null)
Validation --> [*] : Valid name (return args)
@enduml
```

**Validation**: Checks for empty names and duplicate project names.

#### Mark/Unmark Command Flows (Yao Xiang)

Mark and unmark commands follow identical selection flow with different validation:

```plantuml
@startuml Mark/Unmark Command State Diagram
!theme plain

[*] --> ProjectSelection
ProjectSelection --> [*] : Cancelled
ProjectSelection --> TaskDisplay : Project chosen

TaskDisplay --> [*] : Cancelled
TaskDisplay --> TaskSelection : Tasks displayed with [X]/[ ]

TaskSelection --> [*] : Cancelled
TaskSelection --> Validation : Task indices entered

Validation --> TaskSelection : Invalid indices (retry)
Validation --> TaskSelection : Mark already done (retry)
Validation --> TaskSelection : Unmark already not done (retry)
Validation --> [*] : Valid (return args)
@enduml
```

**Shared Logic**: Both commands use identical project/task selection but different validation rules.

#### Sort Command Flow (Yao Xiang)

Sort command offers field and order selection:

```plantuml
@startuml Sort Command State Diagram
!theme plain

[*] --> FieldSelection
FieldSelection --> [*] : Cancelled
FieldSelection --> OrderSelection : Field chosen

OrderSelection --> [*] : Cancelled
OrderSelection --> [*] : Order chosen (return args)
@enduml
```

**Simple Flow**: Two sequential choices with no complex validation.

#### Filter Command Flow (Yao Xiang)

Filter command offers priority level selection:

```plantuml
@startuml Filter Command State Diagram
!theme plain

[*] --> PrioritySelection
PrioritySelection --> [*] : Cancelled
PrioritySelection --> [*] : Priority chosen (return args)
@enduml
```

**Single Choice**: Simple selection from three priority options.

#### List Command Flow (Yao Xiang)

```plantuml
@startuml List Command State Diagram
!theme plain

[*] --> ProjectListDisplay
ProjectListDisplay --> [*] : Cancelled
ProjectListDisplay --> SpecificProject : Project number entered
ProjectListDisplay --> AllProjects : Press enter (empty input)

SpecificProject --> [*] : Display project tasks (return args)
AllProjects --> [*] : Display all projects & tasks (return args)
@enduml
```

**Display Logic**: Shows numbered project list, then either displays tasks for selected project or all projects with all tasks.

#### Status Command Flow (Yao Xiang)


```plantuml
@startuml Status Command State Diagram
!theme plain

[*] --> DisplayChoice
DisplayChoice --> [*] : Cancelled
DisplayChoice --> ProjectStatus : Choose "1. Project status"
DisplayChoice --> TaskStatus : Choose "2. Task status"

ProjectStatus --> [*] : Display stats (return args)
TaskStatus --> [*] : Display completion (return args)
@enduml
```

**Status Types**: Shows either project-level statistics or task completion summaries.

---

## Product scope

---

### Target user profile

{Describe the target user profile}

---

### Value proposition

{Describe the value proposition: what problem does it solve?}

---

## User Stories

| Version | As a ... | I want to ...             | So that I can ...                                           |
| ------- | -------- | ------------------------- | ----------------------------------------------------------- |
| v1.0    | new user | see usage instructions    | refer to them when I forget how to use the application      |
| v2.0    | user     | find a to-do item by name | locate a to-do without having to go through the entire list |

---

## Non-Functional Requirements

{Give non-functional requirements}

---

## Glossary

* *glossary item* - Definition
* dfsdf
*

---

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
