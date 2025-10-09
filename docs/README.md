# Duke

# FlowCLI - Class Documentation

## Overview
FlowCLI is a fast, minimal command-line interface (CLI) task manager that helps users organize tasks within projects. The application follows a clean architecture with separated concerns for commands, UI, data models, and exception handling.

---

## Package Structure

```
app/
├── FlowCLI
command/
├── ArgumentParser
├── Command
├── CommandHandler
├── CommandParser
exception/
├── FlowcliExceptions
Filter/
├── ProjectFilter
project/
├── Project
├── ProjectList
task/
├── Task
├── TaskList
ui/
├── ConsoleUi
```

---

## Class Documentation

### 📦 Package: `app`

#### **FlowCLI**
**Description:** The main entry point of the application. Initializes all core components and starts the command processing loop.

**Responsibilities:**
- Creates and manages the `ProjectList` instance
- Initializes the `ConsoleUi` for user interaction
- Delegates command handling to `CommandHandler`

**How to Use:**
```java
// Run the application
public static void main(String[] args) {
    new FlowCLI().run();
}
```

**Interactions:**
- Creates `ProjectList` to store all projects
- Creates `ConsoleUi` and passes the project list
- Creates `CommandHandler` with both dependencies

---

### 📦 Package: `command`

#### **Command**
**Description:** A simple data class that represents a parsed command with its type and arguments.

**Properties:**
- `type`: The command type (LIST, MARK, UNMARK, ADD, DELETE, BYE, UNKNOWN)
- `arg`: The arguments associated with the command

**How to Use:**
```java
Command cmd = new Command(CommandParser.Type.ADD, "project1 task description");
// Access command type: cmd.type
// Access arguments: cmd.arg
```

**Interactions:**
- Created by `CommandParser`
- Used by `CommandHandler` to determine what action to take

---

#### **CommandParser**
**Description:** Parses raw user input into structured `Command` objects. Identifies the command type and extracts arguments.

**Responsibilities:**
- Split input into command word and arguments
- Map command words to `Command.Type` enum values
- Handle unknown commands

**How to Use:**
```java
String userInput = "add project1 Buy groceries";
CommandParser parser = new CommandParser(userInput, projects);
Command command = parser.getCommand();
// command.type will be Type.ADD
// command.arg will be "project1 Buy groceries"
```

**Static Methods:**
- `parseIndexOrNull(String indexText, int maxIndex)`: Validates and converts string index to zero-based integer

**Interactions:**
- Used by `CommandHandler` to parse each line of user input
- Creates `Command` objects
- Throws exceptions for invalid indices

---

#### **ArgumentParser**
**Description:** Separates project names from task-related arguments. Determines if the first argument is a project name and splits the remaining text.

**Responsibilities:**
- Check if the first word matches an existing project name
- Set `targetProject` if found, or null if not
- Store remaining arguments for further processing

**How to Use:**
```java
String arguments = "project1 task description";
ArgumentParser parser = new ArgumentParser(arguments, projects);

Project target = parser.getTargetProject(); // Returns Project "project1"
String remaining = parser.getRemainingArgument(); // Returns "task description"
```

**Interactions:**
- Used by `CommandHandler` for all commands that need project context
- Queries `ProjectList` to find matching projects
- Enables commands to work with or without project context

---

#### **CommandHandler**
**Description:** The command execution engine. Continuously reads user input, parses commands, and executes corresponding actions.

**Responsibilities:**
- Run the main command loop
- Route commands to appropriate handlers
- Manage exceptions and error messages
- Coordinate between parsing, business logic, and UI

**How to Use:**
```java
CommandHandler handler = new CommandHandler(projects, ui);
handler.handleCommands(); // Starts the command loop
```

**Supported Commands:**
- `list` - Show all projects or tasks in a project
- `add <project>` - Create new project
- `add <project> <task>` - Add task to project
- `mark <project> <index>` - Mark task as done
- `unmark <project> <index>` - Mark task as not done
- `delete <project>` - Delete project
- `delete <project> <index>` - Delete specific task
- `bye` - Exit application

**Interactions:**
- Uses `CommandParser` to parse input
- Uses `ArgumentParser` to extract project and arguments
- Calls `ProjectList` and `Project` methods to modify data
- Uses `ConsoleUi` to display results
- Catches and displays exceptions

---

### 📦 Package: `exception`

#### **FlowcliExceptions**
**Description:** Container class for all custom exception types used in the application. Each exception provides clear error messages to users.

**Exception Types:**

1. **MissingArgumentException**
   - Thrown when: A command requires a project name but none was found
   - Message: "Couldn't find any project with such name"

2. **MissingIndexException**
   - Thrown when: Commands like `mark` or `delete` are missing an index
   - Message: "Please provide an index, e.g., 'mark 2'."

3. **IndexOutOfRangeException**
   - Thrown when: Provided index is outside valid range
   - Message: "Index out of range. Valid: 1..<max>."

4. **EmptyProjectListException**
   - Thrown when: Operations require projects but list is empty
   - Message: "Your Project List is empty. Add a task first."

5. **EmptyTaskListException**
   - Thrown when: Operations require tasks but list is empty
   - Message: "Your Task list is empty. Add a task first."

6. **MissingDescriptionException**
   - Thrown when: Task creation lacks description
   - Message: "Bro stop trolling, you only entered the command and target project..."

7. **UnknownInputException**
   - Thrown when: Command is not recognized
   - Message: "Unknown command, please follow format"

**How to Use:**
```java
if (targetProject == null) {
    throw new FlowcliExceptions.MissingArgumentException();
}
```

**Interactions:**
- Thrown by `CommandParser`, `ArgumentParser`, and `CommandHandler`
- Caught and displayed by `CommandHandler`

---

### 📦 Package: `Filter`

#### **ProjectFilter**
**Description:** Searches for projects whose names contain a specified search string. The search is case-insensitive.

**Responsibilities:**
- Filter projects by name substring
- Return matching projects as a new `ProjectList`

**How to Use:**
```java
ProjectFilter filter = new ProjectFilter(projects, "work");
ProjectList matches = filter.getMatchingTasks(); // Note: method name is misleading
// Returns all projects with "work" in their name (case-insensitive)
```

**Note:** The method `getMatchingTasks()` actually returns projects, not tasks. Consider renaming to `getMatchingProjects()` for clarity.

**Interactions:**
- Takes a `ProjectList` as input
- Creates a new filtered `ProjectList`
- Currently not used in `CommandHandler` but available for search functionality

---

### 📦 Package: `project`

#### **Project**
**Description:** Represents a single project containing a collection of tasks. Each project has a unique name and manages its own `TaskList`.

**Properties:**
- `projectName`: The name of the project
- `projectDescription`: (Currently unused) For future project descriptions
- `projectStatus`: (Currently unused) For future status tracking
- `projectTasks`: The list of tasks belonging to this project

**How to Use:**
```java
Project project = new Project("Work Tasks");

// Add tasks
project.addTask("Complete report");
project.addTask("Send emails");

// Check size
int taskCount = project.size(); // Returns 2

// Access tasks
TaskList tasks = project.getProjectTasks();

// Delete a task
Task deleted = project.deleteTask(0); // Deletes first task
```

**Interactions:**
- Created and managed by `ProjectList`
- Contains a `TaskList` instance
- Used by `CommandHandler` for task operations
- Rendered by `ConsoleUi`

---

#### **ProjectList**
**Description:** Manages the collection of all projects in the application. Provides methods to add, remove, and retrieve projects.

**Responsibilities:**
- Store all projects
- Add new projects
- Delete projects by index or reference
- Find projects by name
- Render all projects for display

**How to Use:**
```java
ProjectList projects = new ProjectList();

// Add projects
projects.addProject("Personal");
projects.addProject("Work");

// Find a project
Project work = projects.getProject("work"); // Case-sensitive!

// Delete a project
Project deleted = projects.delete(work);

// Check size
int count = projects.getProjectListSize();

// Render all
String output = projects.render();
```

**Interactions:**
- Created by `FlowCLI`
- Passed to `ConsoleUi`, `CommandHandler`, and parsers
- Queried by `ArgumentParser` to find projects
- Used by `ProjectFilter` for searching

---

### 📦 Package: `task`

#### **Task**
**Description:** Represents a single task with a description and completion status. This is the basic unit of work in the application.

**Properties:**
- `description`: The task description
- `isDone`: Boolean indicating completion status

**How to Use:**
```java
Task task = new Task("Buy groceries");

// Mark as done
task.mark();
boolean done = task.isDone(); // Returns true

// Unmark
task.unmark();

// Get display string
String display = task.toString(); // "[ ] Buy groceries"
```

**Methods:**
- `mark()`: Set task as complete
- `unmark()`: Set task as incomplete
- `marker()`: Returns "[X]" if done, "[ ]" if not
- `toString()`: Returns formatted task string

**Interactions:**
- Created by `TaskList`
- Modified by `Project` and `TaskList` methods
- Displayed by `ConsoleUi`

---

#### **TaskList**
**Description:** Manages a collection of tasks within a project. Provides CRUD operations for tasks.

**Responsibilities:**
- Store tasks
- Add new tasks
- Mark/unmark tasks
- Delete tasks
- Render task list with numbering

**How to Use:**
```java
TaskList tasks = new TaskList();

// Add tasks
tasks.addTask("Task 1");
tasks.addTask("Task 2");

// Mark task as done (zero-based index)
tasks.mark(0);

// Get a task
Task task = tasks.get(1);

// Delete a task
Task deleted = tasks.delete(0);

// Render with numbering
String display = tasks.render();
// Output:
// 1. [X] Task 1
// 2. [ ] Task 2
```

**Interactions:**
- Created and owned by `Project`
- Accessed through `Project.getProjectTasks()`
- Modified by `CommandHandler`
- Rendered by `ConsoleUi`

---

### 📦 Package: `ui`

#### **ConsoleUi**
**Description:** Handles all user interface interactions. Displays welcome messages, command results, project/task lists, and manages output formatting.

**Responsibilities:**
- Display ASCII art logo and welcome message
- Show success messages for operations
- Display project and task lists
- Print separating lines for readability
- Show goodbye message

**How to Use:**
```java
ConsoleUi ui = new ConsoleUi(projects);

// Show welcome screen
ui.welcome();

// Display project list
ui.showProjectList();

// Display task list for a project
ui.showTaskList(project);

// Show success messages
ui.showAddedProject();
ui.showAddedTask(project);
ui.showMarked("ProjectName", task, true);

// Exit
ui.bye();
```

**Key Methods:**
- `welcome()`: Display logo and greeting
- `bye()`: Display farewell message
- `showProjectList()`: List all projects with their tasks
- `showTaskList(Project)`: List tasks in specific project
- `showMarked(String, Task, boolean)`: Display mark/unmark confirmation
- `showAddedProject()`: Confirm project creation
- `showAddedTask(Project)`: Confirm task addition
- `showDeletedProject(Project)`: Confirm project deletion
- `showDeletedTask(Project, Task)`: Confirm task deletion

**Interactions:**
- Created by `FlowCLI`
- Used extensively by `CommandHandler` to display results
- Accesses `ProjectList` to display lists
- Formats output for better readability

---

## Application Flow

### Startup Sequence
1. **FlowCLI.main()** creates FlowCLI instance
2. **FlowCLI constructor** creates:
   - `ProjectList` (empty initially)
   - `ConsoleUi` (with project list reference)
3. **ConsoleUi.welcome()** displays logo and greeting
4. **FlowCLI.run()** creates `CommandHandler` and starts command loop

### Command Processing Flow
1. **CommandHandler** reads user input
2. **CommandParser** parses command word and arguments → creates `Command`
3. **CommandHandler** switches on command type
4. **ArgumentParser** separates project name from remaining arguments
5. **CommandHandler** executes business logic:
   - Calls methods on `ProjectList` or `Project`
   - Modifies `TaskList` through `Project`
6. **ConsoleUi** displays results
7. Loop continues until "bye" command

### Example: Adding a Task
```
User Input: "add work Complete report"
    ↓
CommandParser → Command(Type.ADD, "work Complete report")
    ↓
ArgumentParser → targetProject="work", remainingArgument="Complete report"
    ↓
CommandHandler → targetProject.addTask("Complete report")
    ↓
ConsoleUi → showAddedTask(targetProject)
```

---

## Developer Guide

### Adding a New Command

1. **Add enum to CommandParser.Type**
```java
public enum Type { LIST, MARK, UNMARK, BYE, ADD, DELETE, FIND, UNKNOWN }
```

2. **Update CommandParser switch statement**
```java
case "find": {
    command = new Command(Type.FIND, arguments);
    break;
}
```

3. **Add handler in CommandHandler**
```java
case FIND: {
    // Your logic here
    break;
}
```

4. **Create UI method if needed**
```java
public void showFindResults(ProjectList results) {
    // Display logic
}
```

### Extending Task Functionality

To add task properties (due dates, priority, etc.):

1. Modify `Task` class to add fields
2. Update `Task.toString()` to display new fields
3. Update `TaskList.addTask()` to accept new parameters
4. Update `CommandHandler` ADD case to parse new fields

### Adding Data Persistence

Currently, data is not saved. To add persistence:

1. Create `Storage` class in a new `storage` package
2. Implement `saveToFile()` and `loadFromFile()` methods
3. Call `storage.loadFromFile()` in `FlowCLI` constructor
4. Call `storage.saveToFile()` after each modification in `CommandHandler`

---

## Best Practices for Contributors

1. **Follow existing patterns**: Commands follow a consistent parse → validate → execute → display pattern
2. **Use exceptions**: Throw appropriate exceptions for error cases
3. **Update UI**: All user-facing changes should go through `ConsoleUi`
4. **Maintain separation**: Keep parsing, business logic, and UI separate
5. **Add JavaDoc**: Document new public methods
6. **Handle edge cases**: Empty lists, null values, invalid indices

---

## Known Limitations

1. **No data persistence**: Projects and tasks are lost when application closes
2. **ProjectFilter unused**: Search functionality exists but not integrated
3. **Case-sensitive project lookup**: "Work" ≠ "work"
4. **Unused Project fields**: `projectDescription` and `projectStatus` defined but not used
5. **Method naming**: `ProjectFilter.getMatchingTasks()` returns projects, not tasks

---

## Future Enhancements

- [ ] File-based data persistence
- [ ] Search/find command using ProjectFilter
- [ ] Task priorities and due dates
- [ ] Project descriptions and status
- [ ] Case-insensitive project names
- [ ] Undo/redo functionality
- [ ] Task filtering within projects
- [ ] Color-coded output
- [ ] Export to CSV/JSON

Useful links:
* [User Guide](UserGuide.md)
* [Developer Guide](DeveloperGuide.md)
* [About Us](AboutUs.md)
