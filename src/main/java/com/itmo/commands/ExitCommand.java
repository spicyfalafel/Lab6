package com.itmo.commands;

/**
 * The type Exit command.
 */
public class ExitCommand extends Command {


    /**
     * Instantiates a new Command.
     *
     * @param args
     */
    public ExitCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }


    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return "byebye";
    }
}
