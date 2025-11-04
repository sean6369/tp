# Yang Zhenzhao - Project Portfolio Page

## Overview

FlowCLI is a Command Line Interface (CLI) application for managing tasks and projects, optimized for fast, fully keyboard-driven workflows. It offers two convenient ways to work: inline commands for power users who want to type everything at once, and interactive mode for guided step-by-step input. Users can track priorities, deadlines and statuses of their projects, filter and sort instantly, then export the current view to a TXT file.

As the technical lead for the foundational architecture, I was responsible for designing and implementing the core command processing infrastructure, project and task management systems, and the status display feature. My contributions focused on creating a robust, extensible architecture that enables efficient command handling and provides users with comprehensive project tracking capabilities.

## Summary of Contributions

### Code Contributed

[RepoSense Link to my code contributions](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=zhenzha0&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

### Enhancements Implemented

#### 1. Command Processing Infrastructure
- **What it does**: Provides a centralized, extensible framework for parsing and executing user commands through `CommandHandler`, `CommandParser`, and `ArgumentParser` classes.
- **Justification**: This infrastructure is critical for maintaining clean separation of concerns and enabling easy addition of new commands without modifying existing code. It supports both inline command mode and interactive mode seamlessly.
- **Highlights**: The design uses the Command pattern to encapsulate command logic, making the system highly maintainable and testable. The `ArgumentParser` utility provides robust flag-based argument parsing that all commands can leverage.
- **Code contributed**: 
  - `CommandHandler` class: Core command execution loop and coordination [\[Functional code\]](../../src/main/java/seedu/flowcli/commands/core/CommandHandler.java)
  - `CommandParser` class: Command parsing and routing logic [\[Functional code\]](../../src/main/java/seedu/flowcli/parsers/CommandParser.java)
  - `ArgumentParser` class: Reusable argument parsing utility [\[Functional code\]](../../src/main/java/seedu/flowcli/parsers/ArgumentParser.java)

#### 2. Core Data Models (Project and Task Management)
- **What it does**: Implements the fundamental `Project`, `ProjectList`, `Task`, and `TaskList` classes that form the backbone of FlowCLI's data management.
- **Justification**: These classes provide a clean, object-oriented approach to managing hierarchical project-task relationships with built-in validation and error handling.
- **Highlights**: Designed with encapsulation principles, ensuring data integrity through controlled access methods. Supports efficient CRUD operations with comprehensive exception handling for edge cases.
- **Code contributed**:
  - `Project` and `ProjectList` classes [\[Functional code\]](../../src/main/java/seedu/flowcli/project/)
  - `Task` and `TaskList` classes [\[Functional code\]](../../src/main/java/seedu/flowcli/task/)

#### 3. Status Display System
- **What it does**: Provides users with visual progress tracking through completion percentages, progress bars, and motivational messages for individual projects or across all projects.
- **Justification**: Essential for users to quickly assess project progress at a glance, improving productivity and project management efficiency.
- **Highlights**: Implements sophisticated status analysis with multiple display formats. The `ProjectStatusAnalyzer` separates analysis logic from presentation, following single-responsibility principles.
- **Code contributed**:
  - `StatusCommand` class [\[Functional code\]](../../src/main/java/seedu/flowcli/commands/StatusCommand.java)
  - `ProjectStatusAnalyzer` utility class [\[Functional code\]](../../src/main/java/seedu/flowcli/commands/utility/ProjectStatusAnalyzer.java)
  - Status-related UI methods in `ConsoleUi` [\[Functional code\]](../../src/main/java/seedu/flowcli/ui/ConsoleUi.java)

#### 4. Data Persistence System
- **What it does**: Implements automatic data storage and loading for all projects and tasks, ensuring data persists between application sessions.
- **Justification**: Critical for production usability - users need their work saved automatically without manual export. Prevents data loss and improves user experience significantly.
- **Highlights**: Implements robust error handling with atomic write operations (temp file + rename), comprehensive data validation, special character escaping, corruption detection with automatic backup, and retry logic for save failures. Handles all edge cases including first-time runs, empty data, corrupted files, and filesystem errors.
- **Code contributed**:
  - `Storage` class with save/load operations [\[Functional code\]](../../src/main/java/seedu/flowcli/storage/Storage.java)
  - `StorageException` and `DataCorruptedException` custom exceptions [\[Functional code\]](../../src/main/java/seedu/flowcli/exceptions/)
  - Integration into `FlowCLI` main class (startup loading) [\[Functional code\]](../../src/main/java/seedu/flowcli/FlowCLI.java)
  - Integration into `ByeCommand` (exit saving with retry) [\[Functional code\]](../../src/main/java/seedu/flowcli/commands/ByeCommand.java)
  - Comprehensive test suite with 24 test cases [\[Test code\]](../../src/test/java/seedu/flowcli/storage/StorageTest.java)

#### 5. Exception Handling Framework
- **What it does**: Implements custom exception classes that provide meaningful error messages to users when validation fails or invalid operations are attempted.
- **Justification**: Proper exception handling is crucial for user experience, helping users understand what went wrong and how to fix it.
- **Code contributed**:
  - `IndexOutOfRangeException`, `ProjectNotFoundException`, `MissingIndexException`, `MissingDescriptionException`, `MissingArgumentException`, `EmptyProjectListException`, `UnknownInputException` [\[Functional code\]](../../src/main/java/seedu/flowcli/exceptions/)

#### 6. User Interface Components
- **What it does**: Implements core UI methods for displaying messages, task lists, and interactive feedback to users.
- **Justification**: Provides consistent, user-friendly console output across all commands.
- **Code contributed**:
  - Basic UI methods in `ConsoleUi` class (welcome messages, task lists, confirmations) [\[Functional code\]](../../src/main/java/seedu/flowcli/ui/ConsoleUi.java)

#### 7. Main Application Class
- **What it does**: Orchestrates the entire application lifecycle from initialization to shutdown, including storage integration for automatic data loading.
- **Justification**: Serves as the entry point and coordinates all major components.
- **Code contributed**:
  - `FlowCLI` main class with storage initialization [\[Functional code\]](../../src/main/java/seedu/flowcli/FlowCLI.java)

### Contributions to the User Guide

- Added Data Persistence section explaining automatic save/load functionality
- Created Common Workflows section with practical command combination examples
- Expanded Troubleshooting section with solutions for parameter validation errors
- Authored Tips and Best Practices section for workflow optimization
- Added export file format example

### Contributions to the Developer Guide

- Documented Command Processing Infrastructure section with architecture design and sequence diagram
- Documented Status Display System section with class diagram and execution flow
- Documented Common Classes section covering core data models
- Documented Data Storage section with implementation details, class diagram, and data format specification

### Contributions to Team-Based Tasks

- **Project Structure Setup**: Established the initial project architecture, including package structure and core class hierarchies
- **Code Architecture Design**: Designed the command processing infrastructure that all team members used when implementing their commands
- **Code Review Standards**: Set up review guidelines and participated in code quality discussions
- **GitHub Pages Configuration**: Created `_config.yml` and configured documentation site with minimalist theme and branding
- **Documentation Branding**: Added FlowCLI cover images and logo across all documentation files

### Review/Mentoring Contributions

While specific PR reviews were conducted throughout the project, my primary mentoring contribution was establishing the foundational architecture and coding patterns that team members could follow when implementing their features. The command processing infrastructure I designed served as a reference implementation for the team.

### Contributions Beyond the Project Team

- **Bug Reports**: Identified and reported bugs in other teams' products during Practical Exam Dry Run (PE-D)
  - [View all bug reports](https://github.com/nus-cs2113-AY2526S1/ped-Zhenzha0/issues)

---

## Contributions to the Developer Guide (Extracts)

### Command Processing Infrastructure

The command processing infrastructure forms the backbone of FlowCLI's architecture, enabling efficient parsing and execution of user commands. This system consists of three main components:

**Architecture Overview:**

1. **CommandHandler**: Coordinates the command execution loop, managing the application's main control flow
2. **CommandParser**: Analyzes user input and creates appropriate Command objects based on the input string
3. **ArgumentParser**: Provides utility methods for extracting flags and arguments from command strings

**Design Rationale:**

The infrastructure follows the Command pattern, where each command encapsulates its own execution logic. This design:
- Enables easy addition of new commands without modifying existing code (Open-Closed Principle)
- Separates command parsing from execution (Single Responsibility Principle)
- Makes commands independently testable
- Supports both inline and interactive command modes through a unified interface

**Sequence Diagram:**

The following sequence diagram illustrates how a user command flows through the system:

![Command Processing Sequence](../plantUML/command-processing-infrastructure/Command-processing-infrastructure-sequence-diagram.png)

**Implementation Details:**

The `ArgumentParser` class provides flag-based argument extraction, which all commands leverage for consistent input handling:

```java
public static String getArgumentForFlag(String input, String flag) 
        throws MissingArgumentException {
    // Extract argument following the specified flag
    // Throws exception if flag is present but argument is missing
}
```

---

### Status Display System

The status display system provides users with comprehensive project progress tracking, featuring completion percentages, visual progress bars, and context-aware motivational messages.

**Architecture Overview:**

The system separates concerns through two main components:
- **StatusCommand**: Handles user interaction and command routing
- **ProjectStatusAnalyzer**: Performs progress calculation and generates status data

**Class Diagram:**

![Status Display Class Diagram](../plantUML/status-display-system/status-display-class-diagram.png)

**Execution Flow:**

When a user executes `status <projectIndex>` or `status --all`, the following sequence occurs:

![Status Command Sequence](../plantUML/status-command-sequence/Status-command-sequence-diagram.png)

**Implementation Details:**

The `ProjectStatusAnalyzer` calculates completion metrics:

```java
public static ProjectStatus analyzeProject(Project project) {
    int totalTasks = project.size();
    int completedTasks = 0;
    
    for (Task task : project.getProjectTasks().getTasks()) {
        if (task.isDone()) {
            completedTasks++;
        }
    }
    
    int percentage = (totalTasks == 0) ? 0 : (completedTasks * 100) / totalTasks;
    return new ProjectStatus(totalTasks, completedTasks, percentage);
}
```

**Display Features:**

1. **Progress Bar**: Visual representation using ASCII characters (`[=====>     ]` format with percentage)
2. **Completion Percentage**: Numerical progress indicator
3. **Motivational Messages**: Context-aware messages based on completion level:
   - ≤25%: "You are kinda cooked, start doing your tasks!"
   - ≤50%: "You gotta lock in and finish all tasks!"
   - ≤75%: "We are on the right track, keep completing your tasks!"
   - >75%: "We are finishing all tasks!! Upzzz!"

---

### Data Storage

The storage system provides persistent data storage for FlowCLI, automatically saving and loading all projects and tasks between sessions.

**Architecture Overview:**

![Storage Class Diagram](../plantUML/data-storage/storage-class-diagram.png)

**Key Components:**
- **Storage**: Main class handling file I/O, validation, and atomic write operations
- **DataCorruptedException**: Thrown when data file format is invalid
- **StorageException**: Thrown on I/O failures (permissions, disk space, etc.)

**Storage Location:** `./data/flowcli-data.txt`

**Data Format:**
```
PROJECT|Project Name
TASK|isDone|description|deadline|priority
```

**Implementation Highlights:**

1. **Atomic Writes**: Data is written to a temp file first, then renamed atomically to prevent corruption
2. **Data Validation**: All loaded data is validated; corrupted files are backed up and user warned
3. **Error Handling**: Retry logic for save failures; graceful degradation on load errors
4. **Edge Cases Handled**: First-time runs, empty data, corrupted files, filesystem errors

---

### Common Classes

The common classes form FlowCLI's core data model, providing robust project and task management capabilities.

**Class Overview:**

- **Project**: Represents a project containing multiple tasks, with methods for task CRUD operations
- **ProjectList**: Manages the collection of all projects with validation and search capabilities
- **Task**: Represents an individual task with description, priority, deadline, and completion status
- **TaskList**: Manages collections of tasks with validation and index-based access

**Design Principles:**

These classes follow object-oriented design principles:
- **Encapsulation**: Internal state is private with controlled access through methods
- **Validation**: All operations include input validation with meaningful exceptions
- **Immutability where appropriate**: Certain fields are final to prevent unintended modifications

**Example Usage:**

```java
// Creating a project with tasks
Project project = new Project("CS2113T Project");
Task task = new Task("Implement feature", Priority.HIGH, "2025-11-15");
project.addTask(task);

// Managing projects
ProjectList projects = new ProjectList();
projects.add(project);
Project retrieved = projects.getProjectByIndex(0);
```

---

## Contributions to the User Guide (Extracts)

### Common Workflows

Here are practical examples of how to combine commands for common use cases:

#### Daily Task Review
```bash
# Check what's urgent
filter-tasks --priority high
status --all

# Review and update a task
list 1
update-task 1 2 --deadline 2025-11-15
mark 1 1
```

#### Weekly Planning
```bash
# See all upcoming deadlines
sort-tasks --deadline ascending

# Export high-priority items for the week
export-tasks weekly-plan.txt filter-tasks --priority high sort-tasks --deadline ascending
```

#### Project Cleanup
```bash
# Check project status
status --all

# Mark completed tasks
mark 1 1
mark 1 3

# Remove finished tasks
delete-task 1 1
```

---

### Tips and Best Practices

#### Workflow Optimization
- **Start each session fresh**: Use `list --all` to check what projects you have
- **Use descriptive project names**: Makes filtering and organizing easier later
- **Set realistic deadlines**: Helps with priority management and sorting
- **Data automatically persists**: Your work is saved automatically when you exit with `bye`

#### Task Management
- **Mark tasks as done regularly**: Helps track progress with the `status` command
- **Use priority levels strategically**: High for urgent tasks, medium for regular tasks, low for nice-to-have items
- **Set deadlines wisely**: Even if approximate, deadlines help with sorting and planning

#### Export Strategies
- **Exports for sharing**: Export creates human-readable files perfect for sharing with team members
- **Filtered exports**: Create focused task lists for specific needs (e.g., high-priority items)
- **Data transfer**: Copy `data/flowcli-data.txt` to move all data to another machine

---
