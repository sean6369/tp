# Yao Xiang's Project Portfolio Page

<img src="../images/yxiang-828.png" width="200"/>

**GitHub**: [Yxiang-828](https://github.com/Yxiang-828)

## Project: FlowCLI

FlowCLI is a desktop task management application designed for university students to organize their academic projects and personal tasks. It features a command-line interface with an optional interactive mode for guided task management, supporting project creation, task CRUD operations, sorting, filtering, and data export capabilities. The application is written in Java and follows the Model-View-Controller architecture pattern.

## Overview

FlowCLI addresses the challenge of managing complex academic or personal projects by providing a streamlined, command-line interface for task and project organization. It helps student developers maintain focus, track progress, and efficiently handle multiple assignments or project phases without the overhead of graphical user interfaces. By offering quick task creation, flexible filtering, and clear status overviews, FlowCLI ensures that users can dedicate more time to coding and less to administrative overhead, ultimately boosting productivity and reducing stress.

## Summary of Contributions

### Code contributed

[RepoSense link](https://nus-cs2113-ay2526s1.github.io/tp-dashboard/?search=yxiang-828&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=&tabOpen=true&tabType=zoom&zA=Yxiang-828&zR=AY2526S1-CS2113-W13-2%2Ftp%5Bmaster%5D&zACS=1113.8333333333333&zS=2025-09-19T00%3A00%3A00&zFS=yxiang-828&zU=2025-10-29T23%3A59%3A59&zMG=false&zFTF=commit&zFGS=groupByRepos&zFR=false)

### Features / Enhancements implemented

#### 1. Sorting / Filtering Inline Commands and Algorithms

- **What it does**: Implements global task sorting and filtering functionality accessible via command-line interface, allowing users to organize and narrow down tasks across all projects.
- **Justification**: Essential feature for task management applications to help users prioritize work and focus on relevant tasks in large project portfolios.
- **Highlights**: Implemented efficient algorithms with O(n log n) sorting complexity and O(n) filtering complexity. Added comprehensive JUnit test coverage with 13 test cases. Integrated parameter validation and error handling for robust user experience.
- **Code contributed**: `TaskSorter.java`, `TaskFilter.java`, `CommandHandler.java` (sort/filter command parsing), comprehensive test suite in `TaskSorterTest.java` and `TaskFilterTest.java`.

#### 2. Interactive Mode Implementation (did the entire feature)

- **What it does**: Transforms single-word commands into guided conversational interfaces, providing step-by-step prompts for complex operations without requiring users to remember full command syntax.
- **Justification**: Reduces learning curve for new users and prevents command errors by guiding users through parameter collection with validation and clear options.
- **Highlights**: Implemented 10+ interactive command flows with tsundere personality prompts, recursive field updates for multi-parameter editing, comprehensive input validation, and proper error handling. Integrated into existing command architecture without breaking backward compatibility.
- **Code contributed**: `InteractivePromptHandler.java` (849+ lines), `CommandHandler.java` integration, all interactive command methods with validation logic.

### Contributions to the User Guide

- Updated command syntax documentation for sort and filter commands
- Added interactive mode usage instructions and examples
- Documented all new command parameters and options

### Contributions to the Developer Guide

- **Architecture Section**: Added detailed layered architecture explanation with interactive mode integration
- **Implementation Section**: Documented sorting/filtering algorithms with complexity analysis, interactive mode implementation details with 4-layer breakdown
- **UML Diagrams**: Created and maintained 14+ PlantUML diagrams including sequence diagrams, state diagrams, class diagrams, and activity diagrams
- **Manual Testing**: Added comprehensive testing instructions with setup steps, sample data, and troubleshooting guide

### Contributions to team-based tasks

- **Documentation Standards**: Ensured AB3 compliance by standardizing author attributions and UML diagram formatting across `DeveloperGuide.md`
- **Code Quality**: Fixed checkstyle violations and maintained coding standards throughout development
- **Testing**: Added comprehensive JUnit tests and manual testing procedures

### Review/mentoring contributions

- Reviewed and merged team member pull requests for error handling and task management features
- Provided guidance on interactive mode implementation and UML diagram standards
- Assisted with merge conflict resolution and branch management

## Key Commits and Pull Requests

### Major Pull Requests:

- **#74**: Developer guide updates with manual testing and UML diagrams
- **#69**: Interactive mode implementation
- **#51**: Full interactive mode feature with personality
- **#19**: Sorting and filtering implementation
- **#18**: Sorting & filtering feature
- **#16**: Help command and error message improvements

### Significant Commits:

- Enhanced interactive mode error handling and user experience (53 lines changed)
- Implemented comprehensive interactive mode for all commands (64 lines changed)
- Added global task sorting and filtering (907 lines changed)
- Added help command and improved error messages (723 lines changed)
- Updated manual testing instructions (121 lines added)
- Standardized UML diagram formatting across all diagrams

## Tools and Technologies

- **PlantUML**: Created professional UML diagrams for documentation
- **JUnit**: Comprehensive test coverage for new features
- **Git/GitHub**: Branch management, pull requests, and code reviews
- **Checkstyle**: Maintained code quality standards
- **Gradle**: Build system and dependency management
