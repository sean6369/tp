package seedu.flowcli.commands;

import seedu.flowcli.commands.core.CommandContext;

/**
 * Base class for FlowCLI commands.
 */
public abstract class Command {

    protected final String arguments;

    protected Command(String arguments) {
        this.arguments = arguments == null ? "" : arguments;
    }

    /**
     * Executes the command.
     *
     * @param context Shared application context.
     * @return {@code true} to continue the command loop, {@code false} to exit.
     * @throws Exception if command execution fails.
     */
    public abstract boolean execute(CommandContext context) throws Exception;
}
