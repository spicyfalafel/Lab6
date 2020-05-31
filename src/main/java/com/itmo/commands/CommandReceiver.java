package com.itmo.commands;

import com.itmo.collection.MyDragonsCollection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * шаблон Команда
 * Получатель ничего не знает о командах. ему дают задание и коллекцию - он выполняет.
 */
public class CommandReceiver implements Serializable {

    private MyDragonsCollection collection;


    /**
     * Gets collection.
     *
     * @return the collection
     */
    public MyDragonsCollection getCollection() {
        return collection;
    }

    /**
     * Instantiates a new Command receiver.
     *
     * @param collection         the collection
     */
    public CommandReceiver(MyDragonsCollection collection){
        this.collection = collection;
    }
    public CommandReceiver(){
        this.collection = new MyDragonsCollection();
    }

    public void setCollection(MyDragonsCollection collection){
        this.collection = collection;
    }


    /**
     * Print help.
     */
    public String printHelp(HashMap<String, Command> registeredCommands){
        //для сортировки по ключу (алфавиту)
        Map<String, Command> treeMap = new TreeMap<>(registeredCommands);
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Command> entry : treeMap.entrySet()){
            builder.append(entry.getKey()).append(" : ").append(entry.getValue().getDescription()).append("\n");
        }
        return builder.toString();
    }

    /**
     * Exit.
     */
    public void exit(){
        System.exit(0);
    }
}