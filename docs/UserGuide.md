# User Guide

## Introduction

FlowCLI is a fast keyboard-driven task manager for teams who organise work around projects. Keep multiple project backlogs in one place, add tasks with deadlines and priorities in a single command, and instantly filter or sort everything you have on your plate. Export the current view to a text file whenever you need a status report.

## Quick Start

1. Ensure Java 17 or a newer Java runtime is installed: `java -version` should report 17.x.
2. Download the latest `flowcli.jar` from [here](https://github.com/AY2526S1-CS2113-W13-2/tp/releases/tag/v1.0).
3. Run the application from a terminal in the same folder as the JAR:
   ```
   java -jar flowcli.jar
   ```
4. Type `help` to see the list of commands. FlowCLI reads commands until you enter `bye`.

Projects and tasks exist only for the current session. Use the export feature to save a snapshot if you need to keep a record.

## Features

### General Command Format

- Commands are case-insensitive, but project names keep their original casing in output.
- Project names and task descriptions can contain spaces. Wrap a multi-word project name in double quotes in any command (e.g. `"Project Alpha"`). Separate options from the description with `--`.
- Deadlines use the `YYYY-MM-DD` format. Priorities accept `low`, `medium`, or `high`.

### Create a project: `create <projectName>`

Adds a new project. If you repeat the command with the same name (any casing), FlowCLI reports a duplicate.

```
create "Birthday Bash"
```

### List projects or tasks: `list [projectName]`

- `list` shows all projects if they have one and clears any previous global filter/sort view.
- `list <projectName>` shows the tasks under a specific project.

```
list "Birthday Bash"
```

### Add a task: `add <projectName> <description> [--priority <level>] [--deadline <YYYY-MM-DD>]`

Adds a task under an existing project with optional priority and deadline. Priority defaults to medium, deadline defaults to none.

```
add "Birthday Bash" Hang fairy lights --priority high --deadline 2025-01-31
```

### Update a task: `update <projectName> <index> [--description <desc>] [--deadline <YYYY-MM-DD|none>] [--priority <level>]`

Edits the specified task in place. You can change one field or combine multiple options in the same command.

- `--description` replaces the task description.
- `--deadline` updates the due date; pass `none` (or `clear`) to remove an existing deadline.
- `--priority` accepts `low`, `medium`, or `high`.

```
update "Birthday Bash" 2 --description Assemble party bags --deadline 2025-02-15 --priority medium
update "Birthday Bash" 3 --deadline none
```

### Mark or unmark a task: `mark <projectName> <index>` / `unmark <projectName> <index>`

Marks the specified task (based on the number shown in `list <projectName>`) as done or not done.

```
mark "Birthday Bash" 2
```

### Delete items: `delete <projectName> [index]`

- `delete <projectName>` removes the entire project and all its tasks.
- `delete <projectName> <index>` removes the indexed task from that project.

```
delete "Birthday Bash" 3
```

### Sort tasks across projects: `sort tasks by <deadline|priority> <ascending|descending>`

Displays every task from every project in the requested order and remembers this view for exporting.

```
sort tasks by deadline ascending
```

### Filter tasks across projects: `filter tasks by <priority|project> <value>`

Shows only tasks that match the chosen priority or project name (case-insensitive). The filtered view can be exported directly.

```
filter tasks by priority high
filter tasks by project "Birthday Bash"
```

### Export tasks: `export tasks to <filename>.txt [projectName] [filter by <type> <value>] [sort by <field> <order>] [--all]`

Saves tasks to a plain-text file.

- Without extra parameters, FlowCLI exports the last sorted or filtered view. If no view exists, it exports every task.
- Include a project name to export just that project:
  ```
  export tasks to party-plan.txt "Birthday Bash"
  ```
- Chain `filter by ...` and/or `sort by ...` to export a customised report:
  ```
  export tasks to high-priority.txt filter by priority high sort by deadline ascending
  ```
- Use `--all` to export everything regardless of the last view:
  ```
  export tasks to audit.txt --all
  ```

### Get help and exit: `help` / `bye`

- `help` reprints the command summary inside the app.
- `bye` exits FlowCLI.

## FAQ

**Q**: How do I transfer my tasks to another machine?  
**A**: FlowCLI currently stores tasks in memory only. Run `export tasks to <filename>.txt --all` and copy the generated text file to the new machine as a snapshot.

**Q**: Why do I get “Couldn't find any project with such name”?  
**A**: The first argument after the command must match an existing project (case-insensitive). Double-check the spelling with `list`.

## Command Summary

| Action | Format | Example |
| --- | --- | --- |
| Create project | `add <projectName>` | `add BirthdayBash` |
| List projects | `list` | `list` |
| List tasks | `list <projectName>` | `list BirthdayBash` |
| Add task | `add <projectName> <desc> [--priority <level>] [--deadline <YYYY-MM-DD>]` | `add BirthdayBash Inflate balloons --priority high --deadline 2024-06-15` |
| Update task | `update <projectName> <index> [--description <desc>] [--deadline <YYYY-MM-DD|none>] [--priority <level>]` | `update BirthdayBash 1 --priority medium` |
| Mark / Unmark | `mark <projectName> <index>` / `unmark <projectName> <index>` | `mark BirthdayBash 1` |
| Delete project | `delete <projectName>` | `delete BirthdayBash` |
| Delete task | `delete <projectName> <index>` | `delete BirthdayBash 2` |
| Sort tasks | `sort tasks by <deadline|priority> <ascending|descending>` | `sort tasks by priority descending` |
| Filter tasks | `filter tasks by <priority|project> <value>` | `filter tasks by priority medium` |
| Export tasks | `export tasks to <filename>.txt [projectName] [filter by …] [sort by …] [--all]` | `export tasks to party-plan.txt "Birthday Bash"` |
| Help / Exit | `help` / `bye` | `help` |
