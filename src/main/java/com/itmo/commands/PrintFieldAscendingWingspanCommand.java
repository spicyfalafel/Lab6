package com.itmo.commands;

/**
 * The type Print field ascending wingspan command.
 */
public class PrintFieldAscendingWingspanCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public PrintFieldAscendingWingspanCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().printFieldAscendingWingspan();
    }

    @Override
    public String getDescription() {
        return "вывести значения поля wingspan в порядке возрастания";
    }
}
