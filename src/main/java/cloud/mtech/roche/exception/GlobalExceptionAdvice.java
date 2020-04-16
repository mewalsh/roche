package cloud.mtech.roche.exception;

import cloud.mtech.roche.controller.ProductController;
import cloud.mtech.roche.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice(basePackageClasses = ProductController.class)
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiError> entityNotFoundException(EntityNotFoundException e) {
        logClientException(e);

        ApiError error = new ApiError(NOT_FOUND.value(), e.getMessage());

        return errorResponse(error);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    public ResponseEntity<ApiError> httpMessageConversionException(HttpMessageConversionException e) {
        logClientException(e);

        ApiError error = new ApiError(BAD_REQUEST.value(), e.getMostSpecificCause().getMessage());

        return errorResponse(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ApiError> constraintViolationException(ConstraintViolationException e) {
        logClientException(e);

        String errorMessage = format("Validation failed. %d error(s)", e.getConstraintViolations().size());
        ApiError error = new ApiError(BAD_REQUEST.value(), errorMessage);

        List<String> errors = e.getConstraintViolations().stream()
          .map(this::buildErrorMsg)
          .collect(toList());
        error.setErrors(errors);

        return errorResponse(error);
    }

    private ResponseEntity<ApiError> errorResponse(ApiError error) {
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    private void logClientException(Exception e) {
        log.info(e.getMessage());
        log.debug("Client side error", e);
    }

    private String buildErrorMsg(ConstraintViolation<?> violation) {
        return format("%s: %s", violation.getPropertyPath(), violation.getMessage());
    }
}
