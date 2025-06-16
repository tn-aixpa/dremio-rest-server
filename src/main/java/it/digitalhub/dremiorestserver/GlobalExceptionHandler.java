// SPDX-FileCopyrightText: © 2025 DSLab - Fondazione Bruno Kessler
//
// SPDX-License-Identifier: Apache-2.0

package it.digitalhub.dremiorestserver;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
    private ResponseEntity<Object> buildResponse(Exception ex, HttpStatus status, WebRequest request, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", formatter.format(Instant.now()));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        if (request instanceof ServletWebRequest) body.put(
            "path",
            ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> buildResponse(Exception ex, HttpStatus status, WebRequest request) {
        return buildResponse(ex, status, request, ex.getMessage());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    ResponseEntity<Object> illegalArgumentHandler(IllegalArgumentException ex, WebRequest request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    ResponseEntity<Object> constraintViolationHandler(ConstraintViolationException ex, WebRequest request) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST, request);
    }
}
