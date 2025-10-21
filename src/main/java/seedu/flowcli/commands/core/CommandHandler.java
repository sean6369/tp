package seedu.flowcli.commands.core;

import java.util.Scanner;

import seedu.flowcli.commands.Command;
import seedu.flowcli.parsers.CommandParser;
import seedu.flowcli.project.ProjectList;
import seedu.flowcli.ui.ConsoleUi;

public class CommandHandler {
    private final ConsoleUi ui;
    private final CommandParser parser;
    private final CommandFactory factory;
    private final CommandContext context;

    public CommandHandler(ProjectList projects, ConsoleUi ui) {
        this.ui = ui;
        ExportCommandHandler exportHandler = new ExportCommandHandler(projects, ui);
        this.parser = new CommandParser();
        this.factory = new CommandFactory();
        this.context = new CommandContext(projects, ui, exportHandler);
    }

    public void handleCommands() {
        Scanner scanner = new Scanner(System.in);
        try {
            boolean shouldContinue = true;
            while (shouldContinue && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) {
                    ui.printLine();
                    continue;
                }

                Command command = resolveCommand(line);
                try {
                    shouldContinue = command.execute(context);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ui.printLine();
                }
            }
        } finally {
            scanner.close();
        }
    }

    private Command resolveCommand(String input) {
        CommandParser.ParsedCommand parsed = parser.parse(input);
        return factory.create(parsed.getType(), parsed.getArguments());
    }
}
