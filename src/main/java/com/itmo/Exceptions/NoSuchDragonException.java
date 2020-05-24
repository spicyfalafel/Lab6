package com.itmo.Exceptions;

public class NoSuchDragonException extends Exception {
    /**
     * пока что используется только при команде findById
     * @param id id дракона, которого не нашли
     */
    public NoSuchDragonException(long id){
        System.out.println("Дракон с id " + id + " не найден.");
    }

}
