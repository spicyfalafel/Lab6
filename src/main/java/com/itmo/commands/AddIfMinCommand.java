package com.itmo.commands;

import com.itmo.collection.Dragon;

import java.util.Scanner;

/**
 * The type Add if min command.
 */
public class AddIfMinCommand extends Command {
    private Dragon dr;
    /**
     * Instantiates a new Command.
     */
    public AddIfMinCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public void clientInsertion() {
        Scanner sc = new Scanner(System.in);
        FieldsScanner helper = new FieldsScanner(sc);
        dr = helper.scanDragon();
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().addIfMin(dr);
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}
