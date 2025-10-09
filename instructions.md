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
- Follow course coding standard
- Proper method header comments (first sentence style)
- Consistent naming conventions
- Appropriate access modifiers

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