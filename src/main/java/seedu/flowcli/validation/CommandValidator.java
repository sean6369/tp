package seedu.flowcli.validation;

import seedu.flowcli.exception.FlowCLIExceptions;

/**
 * Utility class for validating command parameters.
 */
public class CommandValidator {
    
    /**
     * Validates and normalizes priority value.
     * 
     * @param priority The priority string to validate
     * @return Normalized lowercase priority string
     * @throws FlowCLIExceptions.InvalidArgumentException if priority is invalid
     */
    public static String validatePriority(String priority) throws FlowCLIExceptions.InvalidArgumentException {
        if (priority == null) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                "Priority cannot be null. Use low, medium, or high.");
        }
        
        String normalized = priority.toLowerCase().trim();
        for (String validPriority : ValidationConstants.VALID_PRIORITIES) {
            if (normalized.equals(validPriority)) {
                return normalized;
            }
        }
        
        throw new FlowCLIExceptions.InvalidArgumentException(
            "Invalid priority: " + priority + ". Use low, medium, or high.");
    }
    
    /**
     * Converts priority string to integer value.
     * 
     * @param priority The validated priority string
     * @return Integer value (1=low, 2=medium, 3=high)
     */
    public static int priorityToInt(String priority) {
        switch (priority.toLowerCase()) {
        case ValidationConstants.PRIORITY_LOW:
            return ValidationConstants.PRIORITY_LOW_VALUE;
        case ValidationConstants.PRIORITY_MEDIUM:
            return ValidationConstants.PRIORITY_MEDIUM_VALUE;
        case ValidationConstants.PRIORITY_HIGH:
            return ValidationConstants.PRIORITY_HIGH_VALUE;
        default:
            return ValidationConstants.PRIORITY_MEDIUM_VALUE;
        }
    }
    
    /**
     * Validates filter type.
     * 
     * @param filterType The filter type to validate
     * @throws FlowCLIExceptions.InvalidArgumentException if filter type is invalid
     */
    public static void validateFilterType(String filterType) throws FlowCLIExceptions.InvalidArgumentException {
        if (filterType == null) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                "Filter type cannot be null. Use priority or project.");
        }
        
        for (String validType : ValidationConstants.VALID_FILTER_TYPES) {
            if (filterType.equals(validType)) {
                return;
            }
        }
        
        throw new FlowCLIExceptions.InvalidArgumentException(
            "Invalid filter type: " + filterType + ". Use priority or project");
    }
    
    /**
     * Validates sort field.
     * 
     * @param sortField The sort field to validate
     * @throws FlowCLIExceptions.InvalidArgumentException if sort field is invalid
     */
    public static void validateSortField(String sortField) throws FlowCLIExceptions.InvalidArgumentException {
        if (sortField == null) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                "Sort field cannot be null. Use deadline or priority");
        }
        
        for (String validField : ValidationConstants.VALID_SORT_FIELDS) {
            if (sortField.equals(validField)) {
                return;
            }
        }
        
        throw new FlowCLIExceptions.InvalidArgumentException(
            "Invalid sort field: " + sortField + ". Use deadline or priority");
    }
    
    /**
     * Validates sort order.
     * 
     * @param sortOrder The sort order to validate
     * @throws FlowCLIExceptions.InvalidArgumentException if sort order is invalid
     */
    public static void validateSortOrder(String sortOrder) throws FlowCLIExceptions.InvalidArgumentException {
        if (sortOrder == null) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                "Sort order cannot be null. Use ascending or descending");
        }
        
        for (String validOrder : ValidationConstants.VALID_SORT_ORDERS) {
            if (sortOrder.equals(validOrder)) {
                return;
            }
        }
        
        throw new FlowCLIExceptions.InvalidArgumentException(
            "Invalid sort order: " + sortOrder + ". Use ascending or descending");
    }
    
    /**
     * Validates that a filter command is complete at the given index.
     * 
     * @param parts The command parts array
     * @param index The index where "filter" keyword is found
     * @throws FlowCLIExceptions.InvalidArgumentException if filter command is incomplete
     */
    public static void validateFilterCommand(String[] parts, int index) 
            throws FlowCLIExceptions.InvalidArgumentException {
        if (index + 3 >= parts.length || !ValidationConstants.KEYWORD_BY.equals(parts[index + 1])) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                "Incomplete filter command. Use: filter by <type> <value>");
        }
    }
    
    /**
     * Validates that a sort command is complete at the given index.
     * 
     * @param parts The command parts array
     * @param index The index where "sort" keyword is found
     * @throws FlowCLIExceptions.InvalidArgumentException if sort command is incomplete
     */
    public static void validateSortCommand(String[] parts, int index) 
            throws FlowCLIExceptions.InvalidArgumentException {
        if (index + 3 >= parts.length || !ValidationConstants.KEYWORD_BY.equals(parts[index + 1])) {
            throw new FlowCLIExceptions.InvalidArgumentException(
                "Incomplete sort command. Use: sort by <field> <order>");
        }
    }
}
