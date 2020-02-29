package com.hariko.web.filter;

import com.hariko.core.support.HarikoContext;
import com.hariko.web.cnst.WebCnst;
import org.springframework.core.Ordered;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class HttpHeaderFilter extends OncePerRequestFilter {

	private int order = Ordered.HIGHEST_PRECEDENCE + 100;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String traceId = request.getHeader(WebCnst.HEADER_FOR_TRACE_ID);
			if (ObjectUtils.isEmpty(traceId)) {
				traceId = UUID.randomUUID() + "";
			}
			request.setAttribute(HarikoContext.TRACE_ID, traceId);
			HarikoContext.setTraceId(traceId);
			response.addHeader(WebCnst.HEADER_FOR_TRACE_ID, traceId);
			filterChain.doFilter(request, response);
		} finally {
			HarikoContext.removeTraceId();
		}
	}

}
