package com.epam.esm.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.ResourceBundle;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(RestControllerExceptionHandler.class);

    private static final String ERROR_BUNDLE_NAME = "message";
    private static final String ARGUMENT_TYPE_MISMATCH = "exception.methodArgumentType";
    private static final String UNEXPECTED_ERROR = "exception.unexpected";
    private static final String REQUEST_PARAMETER_ERROR = "exception.requestParameter";
    private static final String REQUEST_METHOD_ERROR = "exception.requestMethod";
    private static final String NO_HANDLER_EXCEPTION = "exception.noHandler";
    private static final String NUMBER_FORMAT_ERROR = "exception.numberFormat";

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        LOGGER.error(exception);
        Locale locale = request.getLocale();
        String message = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale)
                .getString(REQUEST_PARAMETER_ERROR);
        int errorCode = status.value();
        String error = errorCode + "05";
        RestControllerException restControllerException = new RestControllerException(error, message);
        return new ResponseEntity<>(restControllerException, new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
        LOGGER.error(exception);
        Locale locale = request.getLocale();
        int errorCode = status.value();
        String error = errorCode + "04";
        String message = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale)
                .getString(NO_HANDLER_EXCEPTION);
        RestControllerException restControllerException = new RestControllerException(error, message);
        return new ResponseEntity<>(restControllerException, new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        LOGGER.error(exception);
        int errorCode = status.value();
        String error = errorCode + "03";
        Locale locale = request.getLocale();
        String message = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale)
                .getString(REQUEST_METHOD_ERROR);
        RestControllerException restControllerException = new RestControllerException(error, message);
        return new ResponseEntity<>(restControllerException, new HttpHeaders(), status);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception,
                                                                    WebRequest request) {
        LOGGER.error(exception);
        Locale locale = request.getLocale();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        int errorCode = status.value();
        String error = errorCode + "02";
        String message = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale)
                .getString(ARGUMENT_TYPE_MISMATCH);
        RestControllerException restControllerException = new RestControllerException(error, message);
        return new ResponseEntity<>(restControllerException, new HttpHeaders(), status);
    }

    @ExceptionHandler({Exception.class})
    private ResponseEntity<Object> handleInternalException(Exception exception, WebRequest request) {
        LOGGER.error(exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int errorCode = status.value();
        String error = errorCode + "01";
        Locale locale = request.getLocale();
        String message = ResourceBundle.getBundle(ERROR_BUNDLE_NAME, locale)
                .getString(UNEXPECTED_ERROR);
        RestControllerException restControllerException = new RestControllerException(error, message);
        return new ResponseEntity<>(restControllerException, new HttpHeaders(), status);
    }
}
