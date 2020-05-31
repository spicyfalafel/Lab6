package com.itmo.commands;

import com.itmo.Exceptions.NoSuchDragonException;

/**
 * The type Remove by id command.
 */
public class RemoveByIdCommand extends Command {


    /**
     * Instantiates a new Command.
     *
     */
    public RemoveByIdCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }


    @Override
    public String execute(CommandReceiver receiver) {
        try{
            long id = Long.parseLong(args[0]);
            if(receiver.getCollection().removeById(id)){
                return "Dragon with id " + args[0] + " removed";
            }else{
                return "No such dragon with id " + args[0];
            }
        }catch (NumberFormatException e){
            return "id - это число большее нуля";
        }
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}
