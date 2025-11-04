package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;
import seedu.flowcli.exceptions.ExtraArgumentException;
import seedu.flowcli.exceptions.StorageException;

import java.util.Scanner;

public class ByeCommand extends Command {

    public ByeCommand(String arguments) {
        super(arguments);
    }

    @Override
    public boolean execute(CommandContext context) throws ExtraArgumentException {
        // Validate that no parameters are provided
        if (arguments != null && !arguments.trim().isEmpty()) {
            throw new ExtraArgumentException("The 'bye' command does not accept any parameters.");
        }
        
        // Save data before exiting
        saveDataWithRetry(context);
        
        context.getUi().printByeMessage();
        return false;
    }

    /**
     * Attempts to save data, with retry option on failure.
     */
    private void saveDataWithRetry(CommandContext context) {
        // Skip saving if Storage is not available (e.g., in tests)
        if (context.getStorage() == null) {
            return;
        }
        
        int maxAttempts = 3;
        int attempt = 1;
        
        while (attempt <= maxAttempts) {
            try {
                context.getStorage().save(context.getProjects());
                return; // Success - exit the retry loop
                
            } catch (StorageException e) {
                System.err.println("============================================================");
                System.err.println("ERROR: Failed to save data! (Attempt " + attempt + "/" + maxAttempts + ")");
                System.err.println("Details: " + e.getMessage());
                System.err.println("============================================================");
                
                if (attempt < maxAttempts) {
                    // Offer retry
                    System.out.print("Retry? (y/n): ");
                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine().trim().toLowerCase();
                    
                    if (!response.equals("y") && !response.equals("yes")) {
                        System.err.println("WARNING: Your data was NOT saved. Changes will be lost.");
                        return; // User chose not to retry
                    }
                    attempt++;
                } else {
                    // Final attempt failed
                    System.err.println("CRITICAL: Data could not be saved after " + maxAttempts + " attempts.");
                    System.err.println("Your changes will be lost.");
                    System.err.println("Consider using 'export-tasks' to manually save your work.");
                    return;
                }
            }
        }
    }
}
