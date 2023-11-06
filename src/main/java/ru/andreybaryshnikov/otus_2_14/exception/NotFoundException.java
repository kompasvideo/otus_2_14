package ru.andreybaryshnikov.otus_2_14.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }
}

