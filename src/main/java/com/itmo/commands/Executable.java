package com.itmo.commands;

import com.itmo.Exceptions.NoSuchDragonException;

/**
 * шаблон команда
 */
public interface Executable {
    /**
     * Execute.
     * @throws NoSuchDragonException the no such dragon exception
     */
    String execute(CommandReceiver receiver) throws NoSuchDragonException;
}
