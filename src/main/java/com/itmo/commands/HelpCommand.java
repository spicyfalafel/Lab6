package com.itmo.commands;

import java.util.HashMap;

/**
 * The type Help command.
 */
public class HelpCommand extends Command {

    /**
     * Instantiates a new Command.
     */
    public HelpCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    private HashMap<String,Command> coms;
    @Override
    public void clientInsertion() {
        coms = CommandsInvoker.getInstance().getMapOfRegisteredCommands();
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.printHelp(coms);
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}
