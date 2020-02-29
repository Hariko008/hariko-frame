package com.hariko.web.filter;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hariko.core.support.HarikoContext;
import org.slf4j.MDC;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import org.springframework.core.Ordered;

public class MdcFilter extends MDCInsertingServletFilter {

    private int order = Ordered.HIGHEST_PRECEDENCE + 200;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            MDC.put(HarikoContext.MDC_TRACE_ID, HarikoContext.getTraceId());

            try {
                super.doFilter(request, response, chain);
            } finally {
                MDC.remove(HarikoContext.MDC_TRACE_ID);
            }
        } else {
            throw new ServletException("Only Http request supported.");
        }
    }

}
