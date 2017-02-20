package com.jtests.api.controllers;

import com.jtests.api.exceptions.ExceptionCodes;
import com.jtests.api.exceptions.ExceptionResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tuanta17
 */
@Controller
public class MainController {

    @RequestMapping("/")
    @ResponseBody
    public ExceptionResponse index() {
        return new ExceptionResponse(ExceptionCodes.UNAUTHORIZED_EXCEPTION, "Unauthorized exception");
    }
}
