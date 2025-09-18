package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.example.util.JwtUtil;

import java.io.IOException;

public class JwtAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Not used
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if (path.endsWith("login.xhtml") || path.endsWith("register.xhtml") ||
                path.endsWith("logout.xhtml") || path.contains("/javax.faces.resource/")) {
            chain.doFilter(req, res);
            return;
        }

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

        if (token == null || !isValidToken(token)) {
            System.err.println("⛔ Invalid or missing JWT token. Redirecting to login.");
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
            return;
        }

        chain.doFilter(req, res);
    }

    private boolean isValidToken(String token) {
        try {
            JwtUtil.validateToken(token);
            return true;
        } catch (Exception e) {
            System.err.println("❌ JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void destroy() {
    }
}



