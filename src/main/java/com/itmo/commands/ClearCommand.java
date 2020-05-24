package com.itmo.commands;

/**
 * The type Clear command.
 */
public class ClearCommand extends Command {


    /**
     * Instantiates a new Command.
     *
     */
    public ClearCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().clear();
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}
