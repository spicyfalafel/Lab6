package com.itmo.commands;

import com.itmo.collection.Dragon;

import java.util.Scanner;

/**
 * The type Update id command.
 */
public class UpdateIdCommand extends Command{

    /**
     * Instantiates a new Command.
     */
    public UpdateIdCommand(String[] args) {
        super(args);
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }
    private Dragon dr;

    @Override
    public void clientInsertion() {
        Scanner sc = new Scanner(System.in);
        FieldsScanner fieldsScanner = new FieldsScanner(sc);
        dr = fieldsScanner.scanDragon();
    }

    /**
     * апдейтит дракона по указанному id. реализован так: сначала удаляет элемент,
     * потом создает новый и присваивает ему id прошлого.
     * @return
     */
    @Override
    public String execute(CommandReceiver receiver) {
        try{
            long id = Long.parseLong(args[0].trim());
            if(receiver.getCollection().findById(id)!=null){
                dr.setId(id);
                receiver.getCollection().add(dr);
                return "Дракон добавлен успешно!";
            }else{
                return "Дракона с id " + id + " в коллекции не нашлось.";
            }
        }catch (NumberFormatException e){
            return "ID - это число";
        }
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}