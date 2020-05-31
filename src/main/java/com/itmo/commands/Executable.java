package com.itmo.commands;

import com.itmo.Exceptions.NoSuchDragonException;

/**
 * шаблон команда
 */
public interface Executable {
    /**
     * Execute.
     *
     */
    String execute(CommandReceiver receiver);
}
