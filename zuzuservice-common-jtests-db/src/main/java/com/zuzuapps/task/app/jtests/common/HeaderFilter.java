package com.zuzuapps.task.app.jtests.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuzuapps.task.app.exceptions.ExceptionCodes;
import com.zuzuapps.task.app.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author tuanta17
 */
public class HeaderFilter implements Filter {

    public static final String X_APP_ID = "x-app-id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String appId = request.getHeader(X_APP_ID);
        if (StringUtils.isEmpty(appId)) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            out.println(mapper.writeValueAsString(new ExceptionResponse(ExceptionCodes.UNAUTHORIZED_EXCEPTION, "Unauthorized exception")));
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
