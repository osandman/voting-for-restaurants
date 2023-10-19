package net.osandman.votingforrestaurants.error;

public class TimeIsOverException extends RuntimeException {
    public TimeIsOverException(String message) {
        super(message);
    }
}
