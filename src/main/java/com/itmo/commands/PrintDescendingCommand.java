package com.itmo.commands;

/**
 * The type Print descending command.
 */
public class PrintDescendingCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public PrintDescendingCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().printDescending();
    }

    @Override
    public String getDescription() {
        return "вывести элементы коллекции в порядке убывания";
    }
}
