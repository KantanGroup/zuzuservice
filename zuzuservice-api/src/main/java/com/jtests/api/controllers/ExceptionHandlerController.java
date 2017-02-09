package com.jtests.api.controllers;

import com.jtests.api.exceptions.ExceptionCodes;
import com.jtests.api.exceptions.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

/**
 * @author tuanta17
 */
@ControllerAdvice
public class ExceptionHandlerController {
    final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ExceptionResponse requestHandlingNoHandlerFound(NoHandlerFoundException e) {
        logger.error("NoHandlerFoundException: " + e, e);
        return new ExceptionResponse(ExceptionCodes.UNAUTHORIZED_EXCEPTION, "Unauthorized exception");
    }

    @ExceptionHandler(value = SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleSQLException(SQLException e) {
        logger.error("SQLException: " + e);
        return new ExceptionResponse(ExceptionCodes.MAINTAIN_EXCEPTION, "Server under maintenance");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleException(Exception e) {
        logger.error("Exception: " + e);
        return new ExceptionResponse(ExceptionCodes.MAINTAIN_EXCEPTION, "Server under maintenance");
    }
}
