package com.itmo.commands;

/**
 * The type Show command.
 */
public class ShowCommand extends Command {


    /**
     * Instantiates a new Command.
     */
    public ShowCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().show();
    }

}
