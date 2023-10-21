package net.osandman.votingforrestaurants.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("Wrong data in request", HttpStatus.NOT_FOUND),
    DATA_CONFLICT("Data integrity error", HttpStatus.CONFLICT),
    BAD_DATA("Wrong data", HttpStatus.UNPROCESSABLE_ENTITY),
    BAD_REQUEST("Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("Request unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("Request forbidden", HttpStatus.FORBIDDEN),
    VALIDATION_ERROR("Validation error", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String title;
    private final HttpStatus httpStatus;

    ErrorType(String title, HttpStatus httpStatus) {
        this.title = title;
        this.httpStatus = httpStatus;
    }
}
