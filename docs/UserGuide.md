# User Guide

## Introduction

FlowCLI is a Command Line Interface (CLI) app for managing tasks and projects, optimised for fast, fully keyboard-driven workflows. It offers two convenient ways to work: **inline commands** for power users who want to type everything at once, and **interactive mode** for guided step-by-step input. Track priorities, deadlines and statuses of your projects, filter and sort instantly, then export the current view to a TXT file to save your changes. If you're a fast typer, FlowCLI is perfect for you - save hours on project management as it gets your work done faster than click-heavy apps.

## Quick Start

1. Ensure Java 17 is installed: `java -version` should report 17.x.
2. Download the latest `flowcli.jar` from [here](https://github.com/AY2526S1-CS2113-W13-2/tp/releases/tag/v2.0).
3. Run the application from a terminal in the same folder as the JAR:
   ```
   java -jar flowcli.jar
   ```
4. Type `help` to see the list of commands. FlowCLI will read commands until you enter `bye`. You can use either inline commands (full syntax) or interactive mode (just type the command name for guided input).

Projects and tasks exist only for the current session. Use the export feature to save a snapshot if you need to keep a record.

## Features

### General Command Format

FlowCLI supports two command modes to suit your preference:

**Inline Commands**: Provide all arguments in one line for quick execution
- Commands are case-insensitive.
- Projects are referenced by their index from `list --all`.
- Task descriptions can contain spaces. Separate options from the description with `--`.
- Deadlines use the `YYYY-MM-DD` format. Priorities accept `low`, `medium`, or `high`.

**Interactive Mode**: Get guided prompts for missing information
- Type just the command name (like `add`, `create`, `update`) and FlowCLI will ask for each required detail
- Perfect for learning commands or when you prefer step-by-step guidance
- All the same validation and features apply

Both modes work identically - choose whichever feels more comfortable!

### Create a project: `create-project <projectName>`

Adds a new project. If you repeat the command with the same name (any casing), FlowCLI reports a duplicate.

```
create-project "Birthday Bash"
```

### List projects or tasks: `list --all` or `list <projectIndex>`

- `list --all` shows all projects and their indices.
- `list <projectIndex>` shows the tasks under a specific project.

```
list 1
```

### Add a task: `add-task <projectIndex> <description> [--priority <level>] [--deadline <YYYY-MM-DD>]`

Adds a task under an existing project with optional priority and deadline. Priority defaults to medium, deadline defaults to none.

```
add-task 1 Hang fairy lights --priority high --deadline 2025-01-31
```

### Update a task: `update-task <projectIndex> <taskIndex> [--description <desc>] [--deadline <YYYY-MM-DD|none>] [--priority <level>]`

Edits the specified task in place. You can change one field or combine multiple options in the same command.

- `--description` replaces the task description.
- `--deadline` updates the due date; pass `none` (or `clear`) to remove an existing deadline.
- `--priority` accepts `low`, `medium`, or `high`.

```
update-task 1 2 --description "Assemble party bags" --deadline 2025-02-15 --priority medium
update-task 1 3 --deadline none
```

### Mark or unmark a task: `mark <projectIndex> <taskIndex>` / `unmark <projectIndex> <taskIndex>`

Marks the specified task (based on the number shown in `list <projectIndex>`) as done or not done.

```
mark 1 2
```

### Delete items: `delete-project <projectIndex> --confirm` or `delete-task <projectIndex> <taskIndex>`

- `delete-project <projectIndex> --confirm` removes the entire project and all its tasks.
- `delete-task <projectIndex> <taskIndex>` removes the indexed task from that project.

```
delete-task 1 3
delete-project 1 --confirm
```

### Sort tasks across projects: `sort-tasks <--deadline|--priority> <ascending|descending>`

Displays every task from every project in the requested order and remembers this view for exporting.

```
sort-tasks --deadline ascending
```

### Filter tasks across projects: `filter-tasks --priority <value>`

Shows only tasks that match the chosen priority. The filtered view can be exported directly.

```
filter-tasks --priority high
```

### Get project status: `status <projectIndex>` or `status --all`

- `status <projectIndex>` shows the completion status for a specific project.
- `status --all` shows the completion status for all projects.

```
status 1
status --all
```

### Export tasks: `export-tasks <filename>.txt [projectIndex] [filter-tasks --priority <value>] [sort-tasks <--deadline|--priority> <order>]`

Saves tasks to a plain-text file.

- Without extra parameters, FlowCLI exports the last sorted or filtered view. If no view exists, it exports every task.
- Include a project index to export just that project:
  ```
  export-tasks party-plan.txt 1
  ```
- Chain `filter-tasks ...` and/or `sort-tasks ...` to export a customised report:
  ```
  export-tasks high-priority.txt filter-tasks --priority high sort-tasks --deadline ascending
  ```

### Get help and exit: `help` / `bye`

- `help` reprints the command summary inside the app.
- `bye` exits FlowCLI.

## Testing Your Setup

Want to make sure FlowCLI is working perfectly? Follow these steps to test all features and get comfortable with the interface.

### Quick Test Setup

1. **Get started fast**: Launch FlowCLI and try these basic commands to verify everything works:
   ```bash
   # Start FlowCLI
   java -jar flowcli.jar

   # Get help (shows all available commands)
   help

   # Create your first project
   create-project "My First Project"

   # Add a sample task
   add-task 1 "Test task" --priority high --deadline 2025-12-31

   # View your project
   list --all

   # Exit when done
   bye
   ```

### Comprehensive Testing Guide

For a thorough test of all FlowCLI features, use this sample dataset:

#### Sample Data Setup

```bash
# Create sample projects
create-project "CS2113T Project"
create-project "Internship Hunt"
create-project "Household Chores"
create-project "Fitness Plan"
create-project "Side Project - Website"

# Add tasks with various priorities and deadlines
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

# Mark some tasks as complete
mark 1 1
mark 2 1
mark 3 3
mark 4 1
```

#### Test All Commands

Now explore FlowCLI's full capabilities:

- **View help anytime**: Type `help` to see all commands and their formats
- **Try inline commands**: Use full command syntax like `add-task 1 "New task" --priority high`
- **Explore interactive mode**: Type just `add`, `create`, `list`, `mark`, `unmark`, `update`, `delete`, `sort`, `filter`, or `export` for guided input
- **Sort and filter**: Try `sort-tasks --deadline ascending` or `filter-tasks --priority high`
- **Check status**: Use `status --all` to see project completion overview
- **Export data**: Run `export-tasks my-tasks.txt` to save your work

### What to Expect

- **Inline commands** execute immediately with all provided information
- **Interactive mode** guides you step-by-step when you provide minimal input
- **Error messages** help you fix mistakes and learn proper syntax
- **Help system** provides comprehensive command reference

### Troubleshooting Tips

- **JAR not found?** Make sure the build completed successfully and you're using the correct path
- **Command errors?** Double-check project/task numbers exist with `list --all`
- **Interactive mode not working?** Ensure you enter commands without arguments to trigger guided input

## FAQ

**Q**: How do I transfer my tasks to another machine?
**A**: FlowCLI currently stores tasks in memory only. Run `export-tasks all_tasks.txt` and copy the generated text file to the new machine as a snapshot.

**Q**: Why do I get an error about an invalid index?  
**A**: Project and task indices must be valid numbers corresponding to the lists. Use `list --all` to see project indices and `list <projectIndex>` for task indices.

**Q**: What's the difference between inline commands and interactive mode?  
**A**: Inline commands require you to type the full command with all arguments (e.g., `add-task 1 "Buy groceries" --priority high`). Interactive mode lets you type just the command name (e.g., `add`) and then guides you through each required input step-by-step. Both modes do exactly the same thing - choose whichever you prefer!## Command Summary

| Action            | Format                                                                                                                | Example                                                                 |
| ----------------- | --------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| Create project    | `create-project <projectName>`                                                                                      | `create-project BirthdayBash`                                         |
| List all projects | `list --all`                                                                                                        | `list --all`                                                          |
| List tasks        | `list <projectIndex>`                                                                                               | `list 1`                                                              |
| Add task          | `add-task <projectIndex> <desc> [--priority <level>] [--deadline <YYYY-MM-DD>]`                                     | `add-task 1 "Inflate balloons" --priority high --deadline 2024-06-15` |
| Update task       | `update-task <projectIndex> <taskIndex> [--description <desc>] [--deadline <YYYY-MM-DD\|none>] [--priority <level>]` | `update-task 1 1 --priority medium`                                   |
| Mark / Unmark     | `mark <projectIndex> <taskIndex>` / `unmark <projectIndex> <taskIndex>`                                           | `mark 1 1`                                                            |
| Delete project    | `delete-project <projectIndex> --confirm`                                                                           | `delete-project 1 --confirm`                                          |
| Delete task       | `delete-task <projectIndex> <taskIndex>`                                                                            | `delete-task 1 2`                                                     |
| Sort tasks        | `sort-tasks <--deadline\|--priority> <ascending\|descending>`                                                         | `sort-tasks --priority descending`                                    |
| Filter tasks      | `filter-tasks --priority <value>`                                                                                   | `filter-tasks --priority medium`                                      |
| Get status        | `status <projectIndex>` / `status --all`                                                                          | `status 1`                                                            |
| Export tasks      | `export-tasks <filename>.txt [projectIndex] [filter-tasks ...] [sort-tasks ...]`                                    | `export-tasks party-plan.txt 1`                                       |
| Help / Exit       | `help` / `bye`                                                                                                    | `help`                                                                |

*Or, simply just use basic commands: `create, list, add, update, mark/unmark, delete, sort, filter, status, export, help, bye`, to easily trigger the interactive prompt*
