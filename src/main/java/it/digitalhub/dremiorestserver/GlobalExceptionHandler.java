package it.digitalhub.dremiorestserver;

import jakarta.validation.ConstraintViolationException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger exceptionsLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00")
        .withZone(ZoneOffset.UTC);

    /**
     * Builds a ResponseEntity with a detailed body.
     *
     * @param ex      Exception.
     * @param status  HTTP status.
     * @param request Web request.
     * @return ResponseEntity with a detailed body.
     */
    private Mono<ResponseEntity<Object>> buildResponse(Exception ex, HttpStatus status, ServerWebExchange request, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", formatter.format(Instant.now()));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        if (request instanceof ServerHttpRequest) body.put(
            "path",
            ((ServerHttpRequest) request).getURI()
        );

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    private Mono<ResponseEntity<Object>> buildResponse(Exception ex, HttpStatus status, ServerWebExchange request) {
        return buildResponse(ex, status, request, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    Mono<ResponseEntity<Object>> illegalArgumentHandler(IllegalArgumentException ex, ServerWebExchange request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }
}
