![FlowCLI Cover](images/logos/FlowCLI-Cover-Image.png)

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
4. You will see flowCLIs greetings, then you may begin typing the commands
5. Type `help` to see the list of commands.
6. FlowCLI will read commands until you enter `bye`. You can use either inline commands (full syntax) or interactive mode (just type the command name for guided input).

Projects and tasks exist only for the current session. Use the export feature to save a snapshot if you need to keep a record.

## Important Notes

### Data Persistence
⚠️ **FlowCLI does not save your data between sessions.** All projects and tasks exist only in memory while the application is running. When you exit with `bye`, all data is lost.

**To preserve your work:**
- Use `export-tasks <filename>.txt` regularly to save snapshots
- Export before closing FlowCLI if you need to keep records
- Consider exporting at the end of each work session

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

### Create a project: `create-project <projectName> / create (interactive mode)`

Adds a new project. If you repeat the command with the same name (any casing), FlowCLI reports a duplicate.

```
create-project "Birthday Bash"
```

### List projects or tasks: `list --all` or `list <projectIndex>` or `list (interactive mode)`

- `list --all` shows all projects and their indices.
- `list <projectIndex>` shows the tasks under a specific project.

```
list 1
```

### Add a task: `add-task <projectIndex> <description> [--priority <level>] [--deadline <YYYY-MM-DD>]` or `add (interactive mode)`

Adds a task under an existing project with optional priority and deadline. Priority defaults to medium, deadline defaults to none.

Structure is felxible such that you can also input `--deadline` first before `--priority` as long as the input are valid  

```
add-task 1 Hang fairy lights --priority high --deadline 2025-01-31
```

### Update a task: `update-task <projectIndex> <taskIndex> [--description <desc>] [--deadline <YYYY-MM-DD|none>] [--priority <level>]` or `update (interactive mode)`

Edits the specified task in place. You can change one field or combine multiple options in the same command.

- `--description` replaces the task description.
- `--deadline` updates the due date; pass `none` (or `clear`) to remove an existing deadline.
- `--priority` accepts `low`, `medium`, or `high`.

```
update-task 1 2 --description "Assemble party bags" --deadline 2025-02-15 --priority medium
update-task 1 3 --deadline none
```

### Mark or unmark a task: `mark <projectIndex> <taskIndex>` / `unmark <projectIndex> <taskIndex>` or `mark/unmark (interactive mode)`


Marks the specified task (based on the number shown in `list <projectIndex>`) as done or not done.

```
mark 1 2
```

### Delete items: `delete-project <projectIndex> --confirm` or `delete-task <projectIndex> <taskIndex>` or `delete (interactive mode)`

- `delete-project <projectIndex> --confirm` removes the entire project and all its tasks.
- `delete-task <projectIndex> <taskIndex>` removes the indexed task from that project.

```
delete-task 1 3
delete-project 1 --confirm
```

### Sort tasks across projects: `sort-tasks <--deadline|--priority> <ascending|descending>` or `sort (interactive mode)`

Displays every task from every project in the requested order and remembers this view for exporting.

```
sort-tasks --deadline ascending
```

### Filter tasks across projects: `filter-tasks --priority <value>` or `filter (interactive mode)`

Shows only tasks that match the chosen priority. The filtered view can be exported directly.

```
filter-tasks --priority high
```

### Get project status: `status <projectIndex>` or `status --all` or `status (interactive mode)`


- `status <projectIndex>` shows the completion status for a specific project.
- `status --all` shows the completion status for all projects.

```
status 1
status --all
```

### Export tasks: `export-tasks <filename>.txt [projectIndex] [filter-tasks --priority <value>] [sort-tasks <--deadline|--priority> <order>]` or `export (interactive mode)`


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

**Export File Format Example:**

When you export tasks, FlowCLI creates a plain text file like this:
```
CS2113T Project - high priority
============================================

[X] [H] Finalize DG (Due: 2025-11-10)
[ ] [H] Implement UI (Due: 2025-11-20)
[ ] [M] Write UG (Due: 2025-11-25)
```

### Get help and exit: `help` / `bye`

- `help` reprints the command summary inside the app.
- `bye` exits FlowCLI.

## Common Workflows

Here are some practical examples of how to combine commands for common use cases:

### Daily Task Review
```bash
# Check what's urgent
filter-tasks --priority high
status --all

# Review and update a task
list 1
update-task 1 2 --deadline 2025-11-15
mark 1 1
```

### Weekly Planning
```bash
# See all upcoming deadlines
sort-tasks --deadline ascending

# Export high-priority items for the week
export-tasks weekly-plan.txt filter-tasks --priority high sort-tasks --deadline ascending
```

### Project Cleanup
```bash
# Check project status
status --all

# Mark completed tasks
mark 1 1
mark 1 3

# Remove finished tasks
delete-task 1 1
```

### Custom Reports
```bash
# Create a report of medium-priority tasks
export-tasks medium-tasks.txt filter-tasks --priority medium

# Create a deadline-sorted report for a specific project
export-tasks project1-deadlines.txt 1 sort-tasks --deadline ascending
```

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

   # Try out interactive mode to create!
   create

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

These are to build up the list of projects and tasks, with the necessary details. You can freely test out

```
sort/filter, delete, update, export
```

 with these however you like.

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

## Troubleshooting

### Common Errors and Solutions

**"Project index is out of range"**
- Check available projects with `list --all`
- Projects are numbered starting from 1
- Make sure you're using the correct index from the list output

**"Task index is out of range"**
- Use `list <projectIndex>` to see all tasks in that project
- Tasks are numbered starting from 1 within each project
- Verify the task hasn't been deleted already

**"Export failed: Permission denied"**
- Ensure you have write permissions in the directory
- Try exporting to a different location (e.g., your home directory)
- On Windows, avoid system directories like `C:\Windows`

**"File is currently open or locked"**
- Close the exported file in other programs (Excel, text editors, etc.)
- Try using a different filename
- Wait a moment and try again

**Interactive mode not triggering?**
- Ensure you type ONLY the command name (e.g., `add` not `add-task`)
- Don't include any arguments or flags to trigger interactive prompts
- Commands that support interactive mode: `create`, `add`, `list`, `mark`, `unmark`, `delete`, `update`, `sort`, `filter`, `export`

**"Invalid date format"**
- Dates must be in `YYYY-MM-DD` format (e.g., `2025-11-15`)
- Use 4-digit year, 2-digit month, 2-digit day
- Separate with hyphens, not slashes

**"Invalid priority value"**
- Priority must be one of: `low`, `medium`, or `high`
- Case-insensitive (LOW, Low, low all work)
- Cannot use abbreviations like `l`, `m`, `h`

## Tips and Best Practices

### Workflow Optimization
- **Start each session fresh**: Use `list --all` to check what projects you have
- **Use descriptive project names**: Makes filtering and organizing easier later
- **Set realistic deadlines**: Helps with priority management and sorting
- **Export regularly**: Since data doesn't persist, save important snapshots throughout your work session
- **Combine filter + sort + export**: Create custom reports for different contexts (meetings, weekly reviews, etc.)

### Command Usage
- **Use interactive mode to learn**: Great for remembering command syntax when starting out
- **Use inline mode for speed**: Once you know the commands, inline is much faster
- **Leverage the help command**: Type `help` anytime to see all available commands and their formats
- **Double-check indices**: Always verify project/task numbers with `list` before deleting

### Task Management
- **Mark tasks as done regularly**: Helps track progress with the `status` command
- **Use priority levels strategically**: 
  - `high` for urgent/important tasks
  - `medium` for regular tasks
  - `low` for nice-to-have items
- **Set deadlines wisely**: Even if approximate, deadlines help with sorting and planning
- **Review status frequently**: Use `status --all` to get a quick overview of all your projects

### Export Strategies
- **End-of-session export**: Always export before typing `bye` to preserve your work
- **Filtered exports**: Create focused task lists for specific needs (e.g., `export-tasks meeting.txt filter-tasks --priority high`)
- **Deadline-sorted exports**: Great for weekly planning (e.g., `export-tasks week.txt sort-tasks --deadline ascending`)
- **Per-project exports**: Export individual projects for team sharing or handoffs

## FAQ

**Q**: How do I transfer my tasks to another machine?
**A**: FlowCLI currently stores tasks in memory only. Run `export-tasks all_tasks.txt` and copy the generated text file to the new machine as a snapshot.

**Q**: Why do I get an error about an invalid index?
**A**: Project and task indices must be valid numbers corresponding to the lists. Use `list --all` to see project indices and `list <projectIndex>` for task indices.

**Q**: What's the difference between inline commands and interactive mode?
**A**: Inline commands require you to type the full command with all arguments (e.g., `add-task 1 "Buy groceries" --priority high`). Interactive mode lets you type just the command name (e.g., `add`) and then guides you through each required input step-by-step. Both modes do exactly the same thing - choose whichever you prefer!



## Command Summary

| Action            | Format                                                                                                               | Example                                                               |
| ----------------- | -------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| Create project    | `create-project <projectName>`                                                                                       | `create-project BirthdayBash`                                         |
| List all projects | `list --all`                                                                                                         | `list --all`                                                          |
| List tasks        | `list <projectIndex>`                                                                                                | `list 1`                                                              |
| Add task          | `add-task <projectIndex> <desc> [--priority <level>] [--deadline <YYYY-MM-DD>]`                                      | `add-task 1 "Inflate balloons" --priority high --deadline 2024-06-15` |
| Update task       | `update-task <projectIndex> <taskIndex> [--description <desc>] [--deadline <YYYY-MM-DD\|none>] [--priority <level>]` | `update-task 1 1 --priority medium`                                   |
| Mark / Unmark     | `mark <projectIndex> <taskIndex>` / `unmark <projectIndex> <taskIndex>`                                              | `mark 1 1`                                                            |
| Delete project    | `delete-project <projectIndex> --confirm`                                                                            | `delete-project 1 --confirm`                                          |
| Delete task       | `delete-task <projectIndex> <taskIndex>`                                                                             | `delete-task 1 2`                                                     |
| Sort tasks        | `sort-tasks <--deadline\|--priority> <ascending\|descending>`                                                        | `sort-tasks --priority descending`                                    |
| Filter tasks      | `filter-tasks --priority <value>`                                                                                    | `filter-tasks --priority medium`                                      |
| Get status        | `status <projectIndex>` / `status --all`                                                                             | `status 1`                                                            |
| Export tasks      | `export-tasks <filename>.txt [projectIndex] [filter-tasks ...] [sort-tasks ...]`                                     | `export-tasks party-plan.txt 1`                                       |
| Help / Exit       | `help` / `bye`                                                                                                       | `help`                                                                |

*Or, simply just use basic commands: `create, list, add, update, mark/unmark, delete, sort, filter, status, export, help, bye`, to easily trigger the interactive prompt*
