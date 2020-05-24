package com.itmo.commands;

import com.itmo.app.Dragon;
import java.util.Date;
import java.util.Scanner;

/**
 * The type Add element command.
 */
public class AddElementCommand extends Command {

    private Dragon dr=null;

    /**
     * Instantiates a new Command.
     */
    public AddElementCommand(String[] args) {

    }

    @Override
    public void clientInsertion() {
        InputHelper inputHelper = new InputHelper(new Scanner(System.in));
        this.dr = inputHelper.scanDragon();
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 0;
    }

    @Override
    public String execute(CommandReceiver receiver){
        dr.setCreationDate(new Date());
        receiver.getCollection().add(dr);
        return "Дракон добавлен успешно!";
    }

    /**
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}
