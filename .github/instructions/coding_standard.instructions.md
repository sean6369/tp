# CS2113 Team Project (tP) - Project Planning and Standards

## Table of Contents
1. [Project Overview](#project-overview)
2. [Team Structure and Roles](#team-structure-and-roles)
3. [Individual Expectations](#individual-expectations)
4. [Team Expectations](#team-expectations)
5. [Functionality Expectations](#functionality-expectations)
6. [Project Schedule Tracking](#project-schedule-tracking)
7. [Issue Tracking Workflow](#issue-tracking-workflow)
8. [Milestone Management](#milestone-management)
9. [Pull Request (PR) Workflow](#pull-request-pr-workflow)
10. [Code Quality Standards](#code-quality-standards)
11. [Testing Requirements](#testing-requirements)
12. [Documentation Standards](#documentation-standards)
13. [Git Workflow](#git-workflow)
14. [Grading Criteria](#grading-criteria)
15. [Common Mistakes to Avoid](#common-mistakes-to-avoid)

## Project Overview

This document outlines the comprehensive planning structure, standards, and procedures for the CS2113 Team Project (tP). The tP follows a breadth-first iterative development approach, aiming to produce working versions of the product incrementally.

**Key Principles:**
- Breadth-first iterative development
- Working MVP at each iteration end
- Cohesive product with consistent quality
- Proper project management and tracking

## Team Structure and Roles

### Recommended Team Structure: Egoless Team
- Every team member is equal in responsibility and accountability
- Consensus-based decision making
- All members contribute ideas and solutions
- Higher risk of conflicts without clear authority figure

### Alternative Structures
1. **Chief Programmer Team**: Single authoritative figure makes major decisions
2. **Strict Hierarchy Team**: Defined organization with clear reporting lines

### Required Roles and Responsibilities
Each important role must be assigned to one person (with optional backup):

**Essential Roles:**
- **Project Lead/Coordinator**: Overall project coordination, milestone tracking
- **Documentation Lead**: Ensures UG/DG consistency and completeness
- **Testing Lead**: Oversees test coverage and CI maintenance
- **Integration Lead**: Manages code integration and build stability
- **Quality Assurance**: Reviews code and ensures standards compliance

**Individual Responsibilities:**
- Contribute to functional code (user-visible features preferred)
- Update User Guide and Developer Guide for added features
- Write automated tests
- Review team members' work
- Carry non-trivial share of team-tasks

## Individual Expectations

### Implementation
- **Functional Code**: Contribute to product features (user-visible preferred)
- **Feature Fit**: Enhancements must fit with product and have team consent
- **Work Distribution**: Divide work by features/enhancements, not components
- **Complete Ownership**: Handle all work related to your enhancement yourself

### Documentation
- **User Guide (UG)**: Update sections related to your enhancements
- **Developer Guide (DG)**: Update with implementation details
- **UML Diagrams**: Use at least 2 types of UML diagrams in DG updates
- **Proposed Features**: Can supplement with documented proposals if needed

### Testing
- **Automated Tests**: Write evidence of automated testing capability
- **Test Coverage**: No minimum requirement, but sufficient to prevent regressions
- **Test Types**: Unit tests, integration tests, system tests as appropriate

### Teamwork
- **Team Tasks**: Carry non-trivial share of team responsibilities
- **Code Reviews**: Review others' work and receive reviews
- **Roles**: Take responsibility for assigned roles

## Team Expectations

### Product Cohesion
- Features integrate to form cohesive product
- Documentation maintains consistent style and presentation
- Demo presents unified product vision

### Product Integrity
- Prevent breaking changes during evolution
- Maintain code quality and stability
- Ensure backward compatibility where needed

### Project Management
- Proper workflow maintenance
- Code maintenance and integration
- Release management
- Schedule adherence

## Functionality Expectations

### Effort Level
- Equivalent to individual iP effort per team member
- No extra marks for exceeding requirements
- Quality over quantity
- Avoid feature bloat

### Grading Basis
- **Effort**: Mostly S/U (Satisfactory/Unsatisfactory)
- **Quality**: Determines implementation marks
- Focus on right-sized, high-quality functionality

### Warning
- >80% of students historically over-deliver
- More features ≠ more marks
- Better to improve other aspects (testing, documentation, reviews)

## Project Schedule Tracking

Using GitHub tools for comprehensive project tracking:

| Tracking Need | GitHub Tool |
|---------------|-------------|
| WHAT needs to be done? | Issues |
| WHO should do tasks? | Issue assignees |
| WHEN to complete tasks? | Milestones with deadlines |
| HOW is task progressing? | Pull Requests |
| HOW is iteration progressing? | Milestones + Releases |

## Issue Tracking Workflow

### Creating Issues
- **Task Size**: Small enough for one person, few hours
- **Descriptive Titles**: Specific and actionable
- **Labels**: type.* and priority.* when applicable
- **Breadth-First**: Each merge improves working version
- **User Stories**: Format as "As a [user] I can [action] so that [benefit]"

### Issue Management
- **Creator**: Preferably the person who will implement
- **Assignment**: Single assignee per issue
- **Milestone Assignment**: Link to iteration milestone
- **Status Tracking**: Open → In Progress → Closed

### Issue Types
- **Features**: New functionality
- **Bugs**: Fixes for existing issues
- **Documentation**: UG/DG updates
- **Testing**: Test implementation
- **User Stories**: Requirements tracking

## Milestone Management

### Creating Milestones
- **Naming**: Use version numbers (v1.0, v1.1, etc.)
- **Deadlines**: Set earlier than course deadlines for buffer
- **Scope**: Represent complete iterations

### Milestone Lifecycle
1. **Planning**: Create milestone, set deadline
2. **Execution**: Assign issues/PRs, track progress
3. **Completion**: All issues closed, release created
4. **Closure**: Milestone closed, incomplete items moved

### Progress Tracking
- Issues assigned to milestone
- PRs linked to milestone
- Progress percentage visible in GitHub
- Release creation for deliverables

## Pull Request (PR) Workflow

### Creating PRs
- **Branch**: Each PR from separate branch (forking workflow)
- **Title**: Match corresponding issue title
- **Description**: Clear description of changes
- **Draft Status**: Use for work-in-progress
- **Issue Linking**: Use "Fixes #123" for auto-close
- **Issue Requirement**: Preferably link PRs to issues (not strictly required, but recommended for tracking)

### PR Review Process
- **Reviewers**: Request reviews from team members
- **Review Guidelines**: Follow SE-EDU best practices
- **Comments**: Location-specific + overall feedback
- **Approval**: "LGTM" (Looks Good To Me)

### Merging PRs
- **CI Status**: Must pass before merging
- **Exceptions**: Only for ignorable failures (documented)
- **Merge Method**: Create merge commit (avoid rebase/squash)
- **Who Merges**: Up to team to decide (PR author, reviewer, team lead)
- **Discouraged**: Unilateral merging without reviews (unless trivial changes)
- **Post-Merge**: Sync repos, update input.txt/EXPECTED.TXT

### Branch Management
- **Keep Branches**: Don't delete after merge (grading requirement)
- **Sync Process**: Pull from team repo, merge master to feature branches

## Code Quality Standards

### Java Standards

#### Naming
- **Packages**: All lowercase, use group/project name as root
  - `seedu.flowcli.command`, `seedu.flowcli.ui`
- **Classes/Enums**: Nouns in PascalCase
  - `Task`, `CommandParser`, `FlowCLI`
- **Variables**: camelCase
  - `taskList`, `isDone`, `projectName`
- **Constants**: SCREAMING_SNAKE_CASE
  - `MAX_ITERATIONS`, `DEFAULT_PRIORITY`
- **Methods**: Verbs in camelCase
  - `getName()`, `addTask()`, `sortByDeadline()`
- **Test Methods**: `featureUnderTest_testScenario_expectedBehavior()`
  - `sortList_emptyList_exceptionThrown()`
- **Booleans**: Use `is`, `has`, `was`, `can` prefixes
  - `isDone`, `hasDeadline`, `wasCompleted`
- **Collections**: Use plural form
  - `List<Task> tasks`, `Set<Project> projects`
- **Iterators**: `i`, `j`, `k` for loops

#### Layout
- **Indentation**: 4 spaces (not tabs)
- **Line Length**: Max 120 characters (soft limit 110)
- **Brackets**: K&R style (Egyptian brackets)
  ```java
  if (condition) {
      statements;
  }
  ```
- **Method Definitions**:
  ```java
  public void someMethod() throws SomeException {
      // body
  }
  ```

#### Statements
- **Conditionals**: Always use braces, condition on separate line
  ```java
  if (condition) {
      statements;
  } else if (otherCondition) {
      statements;
  } else {
      statements;
  }
  ```
- **Loops**: Always use braces
  ```java
  for (int i = 0; i < n; i++) {
      statements;
  }
  ```
- **Switch**: No indentation for case clauses
  ```java
  switch (condition) {
  case ABC:
      statements;
      break;
  default:
      statements;
  }
  ```

#### Comments
- **Language**: English only
- **Javadoc**: First sentence starts with verb (Returns, Adds, etc.)
  ```java
  /**
   * Returns the task description.
   * @param taskId The ID of the task
   * @return Task description string
   */
  public String getDescription(int taskId)
  ```
- **Indentation**: Comments indented relative to code

#### Additional Rules
- Put every class in a package
- Import classes explicitly (not `import java.util.*`)
- Array specifiers attach to type: `int[] array`, not `int array[]`
- All names in English
- Follow Google Java Style Guide for uncovered topics

### Commit Messages
- Follow conventional format
- Descriptive but concise
- Avoid changing timestamps (affects grading)

### Code Reviews
- Mandatory for non-trivial changes
- Focus on correctness, style, and maintainability
- Provide constructive feedback

## Testing Requirements

### Test Coverage
- No minimum percentage required
- Sufficient to prevent regressions
- Focus on critical functionality

### Test Types
- **Unit Tests**: Individual components
- **Integration Tests**: Component interaction
- **System Tests**: End-to-end functionality

### Test Organization
- Test classes in same package as SUT
- Access to package-private members
- Clear test method naming

### CI/CD
- All PRs must pass CI
- Fix failures before merge
- Update test files with new functionality

### JUnit Testing Standards

#### Test Method Naming
- **Format**: `featureUnderTest_testScenario_expectedBehavior()`
- **Examples**:
  - `sortTasks_emptyList_noExceptionThrown()`
  - `filterTasks_byPriorityHigh_returnsHighPriorityTasks()`
  - `addTask_validInput_taskAddedSuccessfully()`

#### Test Structure
- **@BeforeEach/@AfterEach**: Use for setup/cleanup of test fixtures
- **@BeforeAll/@AfterAll**: Use for expensive setup/cleanup (database connections, etc.)
- **@Test**: Mark all test methods with this annotation
- **@DisplayName**: Use descriptive names for complex tests
- **@Disabled**: Temporarily disable failing tests with reason

#### Assertions
- **Use JUnit assertions** in test code, not Java `assert`
- **assertEquals(expected, actual)**: For value comparisons
- **assertTrue/assertFalse**: For boolean conditions
- **assertNotNull/assertNull**: For null checks
- **assertThrows**: For expected exceptions
- **assertDoesNotThrow**: For ensuring no exceptions
- **assertAll**: Group multiple assertions that should all pass

#### Test Data Setup
- **@BeforeEach**: Initialize test fixtures for each test
- **Test Data**: Use realistic but minimal data
- **Isolation**: Each test should be independent
- **Constants**: Define test constants at class level

#### Edge Cases & Error Testing
- **Null Values**: Test with null inputs where applicable
- **Empty Collections**: Test empty lists, sets, maps
- **Boundary Values**: Test minimum/maximum values
- **Invalid Inputs**: Test exception handling
- **Concurrency**: Test thread safety if applicable

#### Test Documentation
- **Comments**: Explain complex test setup or assertions
- **@DisplayName**: Use for tests with complex scenarios
- **Arrange-Act-Assert**: Structure tests clearly

### Assertions Standards

#### When to Use Assertions
- **Internal Invariants**: Verify assumptions about code state
- **Preconditions**: Check method parameters
- **Postconditions**: Verify method results
- **Class Invariants**: Ensure object consistency
- **Control Flow**: Verify execution paths

#### Assertion Syntax
```java
// Basic assertion with message
assert condition : "Error message when assertion fails";

// Common patterns
assert value != null : "Value should not be null";
assert index >= 0 && index < size : "Index out of bounds";
assert state == EXPECTED_STATE : "Invalid state: " + state;
```

#### Best Practices
- **Enable Assertions**: Always run with `-ea` flag in production
- **Don't Use for Control Flow**: Assertions should not affect program logic
- **Clear Messages**: Provide descriptive failure messages
- **Performance**: Assertions have minimal performance impact
- **Testing**: Use JUnit assertions in tests, Java assert in production code

#### Common Assertion Patterns
- **Parameter Validation**: `assert param != null : "Parameter cannot be null";`
- **State Validation**: `assert isValidState() : "Object in invalid state";`
- **Boundary Checks**: `assert index >= 0 : "Index must be non-negative";`

### Logging Standards

#### When to Use Logging
- **Debugging**: Record program execution flow
- **Error Tracking**: Log exceptions and error conditions
- **Audit Trail**: Record important business operations
- **Performance Monitoring**: Track execution times
- **System Health**: Monitor resource usage

#### Logger Setup
```java
import java.util.logging.Logger;
import java.util.logging.Level;

// Class-level logger
private static final Logger logger = Logger.getLogger(ClassName.class.getName());

// Method-level logging
logger.info("Starting operation: " + parameter);
logger.warning("Potential issue detected: " + details);
logger.severe("Critical error occurred", exception);
```

#### Logging Levels
- **SEVERE**: Critical errors that may cause system failure
- **WARNING**: Potential problems that don't stop execution
- **INFO**: General information about program execution
- **CONFIG**: Configuration information
- **FINE/FINER/FINEST**: Detailed debugging information

#### Best Practices
- **Consistent Format**: Use structured logging with context
- **Avoid Sensitive Data**: Don't log passwords, tokens, or PII
- **Performance**: Use appropriate levels to avoid overhead
- **Exception Logging**: Include stack traces for errors
- **Resource Management**: Ensure log files don't grow unbounded

#### Logging Patterns
```java
// Entry/Exit logging
logger.entering(className, methodName, parameters);
logger.exiting(className, methodName, result);

// Exception logging
try {
    riskyOperation();
} catch (Exception e) {
    logger.log(Level.SEVERE, "Operation failed", e);
}

// Performance logging
long startTime = System.nanoTime();
// ... operation ...
long duration = System.nanoTime() - startTime;
logger.info("Operation completed in " + duration + " ns");
```

## Documentation Standards

### User Guide (UG)
- Clear, user-focused instructions
- Updated for all new features
- Consistent formatting and style
- Include examples and screenshots

### Developer Guide (DG)
- Technical implementation details
- Architecture decisions
- UML diagrams (minimum 2 types per contributor)
- Design rationale and alternatives

### Documentation Quality
- Professional presentation
- Consistent voice and terminology
- Accurate and up-to-date
- Cross-referenced where appropriate

## Git Workflow

### Branching Strategy
- **Forking Workflow**: Required for early iterations
- **Feature Branches**: One branch per PR
- **Master Branch**: Always deployable

### Commit Practices
- Frequent, atomic commits
- Clear commit messages
- Don't change existing commit timestamps

### Synchronization
- Regular pulls from team repository
- Merge master into feature branches
- Push updates to fork

## Grading Criteria

### Individual Components
- **Implementation**: Code quality and functionality
- **Documentation**: UG and DG completeness
- **Testing**: Automated test coverage
- **Teamwork**: Reviews and task completion
- **Project Management**: Issue/PR management

### Team Components
- **Product Cohesion**: Integrated, consistent product
- **Quality Maintenance**: Stability and reliability
- **Project Management**: Workflow and schedule adherence

### Grading Scale
- **Effort**: Primarily S/U basis
- **Quality**: Determines final marks
- **Peer Evaluation**: Team member assessments

## Common Mistakes to Avoid

### Git Workflow
- ❌ Sending PRs from master branch
- ❌ Rebasing/squashing commits (changes timestamps)
- ❌ Not using separate branches for each PR
- ❌ Deleting merged branches

### Project Management
- ❌ Depth-first development approach
- ❌ Not tracking tasks with issues
- ❌ Missing milestones and deadlines
- ❌ Not reviewing PRs

### Code Quality
- ❌ Wrong commit message format
- ❌ Incorrect JavaDoc first sentence style
- ❌ Going "rogue" with unapproved features
- ❌ Not updating documentation

### Teamwork
- ❌ Everyone responsible for everything (no clear roles)
- ❌ Not carrying fair share of team tasks
- ❌ Not reviewing others' work
- ❌ Feature bloat beyond requirements

---

## Quick Reference

### Daily Workflow
1. Check assigned issues
2. Create feature branch for new work
3. Implement changes with tests
4. Update documentation
5. Create PR and request review
6. Address review feedback
7. Merge after approval

### Weekly Cadence
- Monday: Plan week, assign issues
- Daily: Standup, progress updates
- Friday: Review progress, adjust plans

### Emergency Procedures
- Build failures: Fix immediately
- CI failures: Address before merge
- Conflicts: Communicate and resolve quickly
- Missed deadlines: Reassign and adjust scope

---

*This document serves as the comprehensive guide for tP execution. Update as needed for team-specific adaptations while maintaining compliance with course requirements.*