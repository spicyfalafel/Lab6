package com.itmo.commands;

import com.itmo.collection.Dragon;

import java.util.Scanner;

/**
 * The type Remove lower element command.
 */
public class RemoveLowerThanElementCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public RemoveLowerThanElementCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    private Dragon dr;
    @Override
    public void clientInsertion() {
        Scanner sc = new Scanner(System.in);
        FieldsScanner helper = new FieldsScanner(sc);
        dr = helper.scanDragon();
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().removeLower(dr);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
