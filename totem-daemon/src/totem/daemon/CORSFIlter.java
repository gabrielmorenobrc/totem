package totem.daemon;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gmoreno on 30/09/2017.
 */
public class CORSFIlter implements Filter {

    public CORSFIlter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String value = httpServletRequest.getHeader("Access-Control-Request-Headers");
        if (value != null && !value.isEmpty()) {
            ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Origin,Content-Type");
        }
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Origin", "*");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
