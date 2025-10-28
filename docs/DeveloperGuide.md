# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

### Update Task Feature by [Zing Jen](team/zingjen.md)

#### Overview

The `update` command allows users to modify the attributes of an existing task within a project. Users can update the task's description, deadline, and priority.

#### How it works

The `update` command follows a structured process to parse user input, validate it, and apply the requested changes. The implementation is primarily located in the `seedu.flowcli.commands.UpdateCommand` class.

Here's a sequence diagram illustrating the process:

```
sequenceDiagram
    actor User
    User->>FlowCLI: update p/PROJECT_NAME 1 --description New description --deadline 2025-12-31 --priority high
    FlowCLI->>CommandParser: parse("update ...")
    CommandParser->>UpdateCommand: new UpdateCommand("p/PROJECT_NAME ...")
    FlowCLI->>UpdateCommand: execute(context)
    UpdateCommand->>ArgumentParser: new ArgumentParser("p/PROJECT_NAME ...")
    ArgumentParser->>UpdateCommand: return parsedArgument
    UpdateCommand->>CommandParser: parseIndexOrNull("1", project.size())
    CommandParser->>UpdateCommand: return 1
    UpdateCommand->>CommandValidator: validatePriority("high")
    CommandValidator->>UpdateCommand: return "high"
    UpdateCommand->>Project: updateTask(1, "New description", true, 2025-12-31, true, 3, true)
    Project->>Task: update(...)
    Task->>Project: return updatedTask
    Project->>UpdateCommand: return updatedTask
    UpdateCommand->>ConsoleUi: showUpdatedTask(project, updatedTask)
    ConsoleUi->>User: Display updated task
```

**Step-by-step breakdown:**

1.  **User Input**: The user enters the `update` command with the project name, task index, and the fields to be updated (e.g., `--description`, `--deadline`, `--priority`).
2.  **Parsing**: The `CommandParser` identifies the command as `update` and creates an `UpdateCommand` object with the arguments.
3.  **Execution**: The `FlowCLI` main loop calls the `execute` method of the `UpdateCommand`.
4.  **Argument Parsing**: The `UpdateCommand` uses an `ArgumentParser` to separate the project identifier from the rest of the arguments.
5.  **Task Index Parsing**: The task index is parsed from the remaining arguments.
6.  **Option Parsing**: The command then parses the update options (`--description`, `--deadline`, `--priority`) and their corresponding values.
7.  **Validation**: Input values are validated. For example, the priority level is checked to be one of the allowed values (`low`, `medium`, `high`).
8.  **Updating the Task**: The `updateTask` method of the `Project` object is called. This method retrieves the `Task` object and updates its attributes.
9.  **UI Feedback**: The `ConsoleUi` is used to display the updated task details to the user, confirming that the operation was successful.

#### Design Considerations

- **Clear Separation of Concerns**: The `UpdateCommand` is responsible for the command's logic, while the `ArgumentParser` handles the parsing of arguments. This separation makes the code easier to understand and maintain.
- **Robust Error Handling**: The command includes checks for various error conditions, such as missing arguments, invalid task indices, and incorrect option formats. This ensures that the user receives clear feedback in case of invalid input.
- **Extensibility**: The use of option flags (`--description`, etc.) makes it easy to add more updatable fields in the future without changing the command's fundamental structure.

#### Alternatives Considered

- **Single-purpose update commands**: An alternative was to have separate commands for updating each field (e.g., `update-description`, `update-deadline`). This was rejected as it would lead to a larger number of commands, making the CLI more complex for the user. The current approach of using flags provides a more consolidated and user-friendly experience.
- **Interactive prompt**: Another option was to guide the user through an interactive prompt for updating a task. While this could be user-friendly for beginners, it would be less efficient for experienced users who prefer to perform actions with a single command. The current flag-based approach is faster for users who are familiar with the command syntax.

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
