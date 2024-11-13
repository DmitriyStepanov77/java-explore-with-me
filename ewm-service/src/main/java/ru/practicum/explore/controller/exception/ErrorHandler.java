package ru.practicum.explore.controller.exception;

import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.dto.exception.ErrorDto;
import ru.practicum.explore.exception.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequest(final Exception e) {
        log.error(e.getMessage());
        String errorMessage;
        List<String> errors = new ArrayList<>();
        String reason;

        if (e instanceof MethodArgumentNotValidException ex) {
            errors = ex.getBindingResult().getFieldErrors().stream()
                    .map(fieldError -> String.format("Field '%s': %s", fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());
            errorMessage = "Validation failed for one or more fields";
            reason = "MethodArgumentNotValidException";
        } else if (e instanceof MissingServletRequestParameterException ex) {
            errorMessage = String.format("Required parameter is missing: %s", ex.getParameterName());
            reason = "MissingServletRequestParameterException";
        } else {
            errorMessage = e.getMessage();
            reason = "ValidationException";
        }
        return ErrorDto.builder()
                .errors(errors)
                .message(errorMessage)
                .reason(reason)
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFound(final NotFoundException e) {
        log.error(e.getMessage());
        return ErrorDto.builder()
                .errors(new ArrayList<>())
                .message(e.getMessage())
                .reason("NotFoundException")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class,
            UserIsNotInitiatorException.class, UpdateEventIsPublishedException.class,
            UpdateEventTimeIncorrectException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleConflict(final Exception e) {
        log.error(e.getMessage());

        String errorMessage = e.getMessage();
        List<String> errors = new ArrayList<>();
        String reason;

        if (e instanceof DataIntegrityViolationException)
            reason = "DataIntegrityViolationException";
        else if (e instanceof ConflictException)
            reason = "ConflictException";
        else if (e instanceof UserIsNotInitiatorException)
            reason = "UserIsNotInitiatorException";
        else if (e instanceof UpdateEventIsPublishedException)
            reason = "UpdateEventIsPublishedException";
        else if (e instanceof UpdateEventTimeIncorrectException)
            reason = "UpdateEventTimeIncorrectException";
        else
            reason = "ConflictException";
        return ErrorDto.builder()
                .errors(errors)
                .message(errorMessage)
                .reason(reason)
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
