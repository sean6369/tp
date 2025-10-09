## Feature: Global Task Sorting and Filtering

Implement global sorting and filtering capabilities for tasks across all projects in FlowCLI.

### Commands:
- `sort tasks by <field> <order>` - Sort tasks by deadline or priority in ascending/descending order
  - field: deadline / priority
  - order: asc/desc
- `filter tasks by priority <level>` - Filter tasks by priority level (high/medium/low)
  - level: high / medium / low
- `filter tasks by project <id>` - Filter tasks by project ID
  - id: project index number

### Expected Behavior:
- `sort tasks` sorts the global task list by deadline or priority, with configurable ascending/descending order
- `filter tasks` filters the global task list by project ID and/or priority level
- Both commands should work across all projects and display results with project context
- Null deadlines should be handled appropriately (last in ascending, first in descending)

### JUnit Testing Checklist:
- [ ] `testSortByDeadlineAscending` - Verifies tasks sorted by earliest deadline first, nulls last
- [ ] `testSortByDeadlineDescending` - Verifies tasks sorted by latest deadline first, nulls first
- [ ] `testSortByPriorityAscending` - Verifies tasks sorted from low to high priority
- [ ] `testSortByPriorityDescending` - Verifies tasks sorted from high to low priority
- [ ] `testFilterByPriorityHigh` - Filters for high priority tasks (priority 3)
- [ ] `testFilterByPriorityMedium` - Filters for medium priority tasks (priority 2)
- [ ] `testFilterByPriorityLow` - Filters for low priority tasks (priority 1)
- [ ] `testFilterByProjectId` - Filters tasks from specific project (ID 1)
- [ ] `testFilterByProjectId2` - Filters tasks from another project (ID 2)
- [ ] `testFilterByPriorityAndProject` - Filters by both priority and project ID