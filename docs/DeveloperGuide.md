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
    - [Task Sorting Algorithm](#task-sorting-algorithm-yao-xiang)
    - [Task Filtering Algorithm](#task-filtering-algorithm-yao-xiang)
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

![Architecture Diagram](plantUML/architecture/architecture.png)

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

![Task Sorting Algorithm](plantUML/task-sortiing-algo/task-sorting-algo.png)

**Algorithm Details:**

- **Time Complexity**: O(n log n) using Java's built-in sort
- **Space Complexity**: O(n) for task list copy
- **Deadline Handling**: Tasks without deadlines are sorted last in ascending order
- **Priority Mapping**: High(1) > Medium(2) > Low(3)

#### Task Filtering Algorithm (Yao Xiang)

The filtering algorithm supports filtering tasks by priority level and/or project name:

![Task Filtering Algorithm](plantUML/task-filtering-algo/task-filtering-algo.png)

**Algorithm Details:**

- **Time Complexity**: O(n) linear scan through all tasks
- **Space Complexity**: O(m) where m is number of matching tasks
- **Case Insensitive**: Project name and priority filtering ignore case
- **Multiple Filters**: Can combine priority and project name filters

### Data Persistence

Export: Export current project and task data to a file.

### **User Interface**

### Interactive Mode (Yao Xiang)

![Interactive Mode Overview](plantUML/interactive-mode-overview/interactive-mode-overview.png)

#### Implementation Overview

The interactive mode transforms single-word commands into guided conversations. When a user types "add" without arguments, the system prompts for project selection, task details, and optional fields.

#### Command Processing Sequence (Yao Xiang)

The overall command processing workflow shows how user input flows through the system components:

![Command Processing Sequence Diagram](plantUML/command-processing-sequence/Command%20Processing%20Sequence%20Diagram.png)

**Architecture Flow**: User input → CommandHandler → InteractivePromptHandler (if needed) → CommandFactory → Command execution → Result display.

#### Class Diagram: InteractivePromptHandler Structure (Yao Xiang)

![InteractivePromptHandler Class Diagram](plantUML/interactive-prompt-handler/interactiveprompthandler.png)

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

![Add Command Sequence Diagram](plantUML/add-command-sequence/Add%20Command%20Sequence%20Diagram.png)

**Key Features**:

- Project validation with range checking
- Required task description with empty string rejection
- Optional priority (defaults to "medium")
- Optional deadline with YYYY-MM-DD format validation

#### Add Command State Flow (Yao Xiang)

![Add Command State Diagram](plantUML/add-command-state/add-command-state.png)

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

![Delete Command State Diagram](plantUML/delete-command-state/delete-command-state.png)

**Safety Features**:

- Confirmation prompts for all destructive operations
- Clear project/task listing before selection
- Case-insensitive confirmation ("y", "yes", "n", "no")

#### Update Command Flow (Yao Xiang)

Update command implements recursive field updates allowing multiple changes in one session:

![Update Command State Diagram](plantUML/update-command-state/update-command-state.png)

**Recursive Design**: Users can update multiple fields without restarting the flow, with options to reselect tasks/projects or exit at any point.

#### Export Command Flow (Yao Xiang)

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


#### Create Command Flow (Yao Xiang)

Create command prompts for a new project name with validation:

![Create Command State Diagram](plantUML/create-command-state/create-command-state.png)

**Validation**: Checks for empty names and duplicate project names.

#### Mark/Unmark Command Flows (Yao Xiang)

Mark and unmark commands follow identical selection flow with different validation:

![Mark/Unmark Command Sequence Diagram](plantUML/mark-unmark-sequence/Unmark%20Command%20Sequence%20Diagram.png)

**Shared Logic**: Both commands use identical project/task selection but different validation rules.

#### Sort Command Flow (Yao Xiang)

Sort command offers field and order selection:

![Sort Command State Diagram](plantUML/sort-command-state/sort-command-state.png)

**Simple Flow**: Two sequential choices with no complex validation.

#### Filter Command Flow (Yao Xiang)

Filter command offers priority level selection:

![Filter Command State Diagram](plantUML/filter-command-state/filter-command-state.png)

**Single Choice**: Simple selection from three priority options.

#### List Command Flow (Yao Xiang)

![List Command State Diagram](plantUML/list-command-state/list-command-state.png)

**Display Logic**: Shows numbered project list, then either displays tasks for selected project or all projects with all tasks.

#### Status Command Flow (Yao Xiang)

![Status Command State Diagram](plantUML/status-command-state/status-command-state.png)

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
