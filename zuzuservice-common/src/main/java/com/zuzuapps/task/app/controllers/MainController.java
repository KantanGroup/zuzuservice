package com.zuzuapps.task.app.controllers;

import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.ExceptionResponse;
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
