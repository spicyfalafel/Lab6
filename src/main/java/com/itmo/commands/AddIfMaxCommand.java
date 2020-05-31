package com.itmo.commands;

import com.itmo.collection.Dragon;

import java.util.Scanner;

/**
 * The type Add if max command.
 */
public class AddIfMaxCommand extends Command{
    private Dragon dr;

    @Override
    public void clientInsertion() {
        Scanner sc = new Scanner(System.in);
        FieldsScanner helper = new FieldsScanner(sc);
        dr = helper.scanDragon();
    }

    /**
     * Instantiates a new Command.
     *
     * @param args
     */

    public AddIfMaxCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(CommandReceiver receiver) {
        return receiver.getCollection().addIfMax(dr);
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }
}
