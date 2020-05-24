package com.itmo.commands;

import java.io.Serializable;

/**
 * шаблон Команда
 */
public abstract class Command implements Executable, Serializable {
    /**
     * The Receiver.
     */
    protected String[] args;

    public void clientInsertion(){
    }

    /**
     * у команд типа AddElement, AddIfMin, AddIfMax, UpdateIdCommand метод возвращает 0, т.к.
     * элемент вводится построчно
     *
     * @return количество аргументов у команды
     */
    abstract public int getNumberOfRequiredArgs();

    /**
     * Instantiates a new Command.
     *
     */
    public Command(String[] args){
        this.args = args;
    }

    public Command(){}

    public void setArgs(String[] args){
        this.args = args;
    }
    /**
     * Get description string.
     *
     * @return описание команды
     */
    public String getDescription(){
        return "ленивый разработчик не написал описание команды";
    }
}
