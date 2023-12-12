package ru.andreybaryshnikov.otus_2_14.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message) {
        super(message);
    }
}
