package com.sportradar.library;

class ScoreboardException extends Exception {
    public ScoreboardException(String message) {
        super(message);
    }

    public ScoreboardException(String message, Throwable cause) {
        super(message, cause);
    }
}