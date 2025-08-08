package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.example.util.JwtUtil;  // your JWT util class

public class JwtAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no init needed here
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        // Allow public pages and resources (adjust these as per your app)
        if (path.endsWith("login.xhtml") || path.endsWith("register.xhtml") ||
                path.contains("/javax.faces.resource/") || path.endsWith("logout.xhtml")) {
            chain.doFilter(req, res);
            return;
        }

        // Get token cookie
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Check token validity
        if (token == null || !isValidToken(token)) {
            // No token or invalid token: redirect to login page
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
            return;
        }

        // Token valid: continue request
        chain.doFilter(req, res);
    }

    private boolean isValidToken(String token) {
        try {
            JwtUtil.validateToken(token);
            return true;
        } catch (Exception e) {
            // token invalid or expired
            return false;
        }
    }

    @Override
    public void destroy() {
        // no clean up needed
    }
}


