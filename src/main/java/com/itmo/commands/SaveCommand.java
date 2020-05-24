package com.itmo.commands;

import com.itmo.app.XmlStaff;
import com.itmo.server.Server;

import java.io.FileNotFoundException;

/**
 * The type Save command.
 */
public class SaveCommand extends Command {

    /**
     * Instantiates a new Command.
     *
     */
    public SaveCommand(String[] args) {
        super(args);
    }
    public SaveCommand(){
        this.args = new String[]{"input.xml"};
    }
    public SaveCommand(String resFileName){
        this.args = new String[]{resFileName};
    }

    @Override
    public int getNumberOfRequiredArgs() {
        return 1;
    }

    @Override
    public String execute(CommandReceiver receiver) {
        try{
            XmlStaff.writeCollectionToFile(receiver.getCollection().getDragons(), args[0]);
            Server.log.info("save done");
            return "save done";
        }catch (FileNotFoundException e){
            Server.log.info("файл для сохранения не найден. возможно препод убрал права на файл или директорию.");
            return "файл для сохранения не найден.";
        }
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }
}
