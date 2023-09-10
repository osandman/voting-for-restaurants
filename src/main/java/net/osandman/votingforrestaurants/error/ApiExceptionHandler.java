package net.osandman.votingforrestaurants.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static net.osandman.votingforrestaurants.error.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorInfo notFoundError(HttpServletRequest req, NoSuchElementException e) {
        return logAndGetErrorInfo(req, e, DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    public ErrorInfo bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, VALIDATION_ERROR, details);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    public ErrorInfo validationError(HttpServletRequest req, ConstraintViolationException e) {
        String[] details = e.getConstraintViolations().stream()
                .map(cv -> String.format("[%s] %s", cv.getPropertyPath(), cv.getMessage()))
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, VALIDATION_ERROR, details);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = getRootCause(e).getMessage();
        if (rootMsg != null) {
            return logAndGetErrorInfo(req, e, VALIDATION_ERROR, rootMsg);
        }
        return logAndGetErrorInfo(req, e, DATA_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ErrorInfo defaultErrorHandler(HttpServletRequest req, Exception e) {
        e = (Exception) getRootCause(e);
        if (e instanceof DataIntegrityViolationException exception) {
            return conflict(req, exception);
        } else if (e instanceof ConstraintViolationException exception) {
            return validationError(req, exception);
        } else if (e instanceof BindException exception) {
            return bindValidationError(req, exception);
        } else if (e instanceof NoSuchElementException exception) {
            return notFoundError(req, exception);
        }
        return logAndGetErrorInfo(req, e, APP_ERROR);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Throwable e, ErrorType errorType, String... details) {
        Throwable rootCause = getRootCause(e);
        log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        return new ErrorInfo(req.getRequestURL(), errorType,
                details.length != 0 ? details : new String[]{rootCause.toString()});
    }

    private static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}
