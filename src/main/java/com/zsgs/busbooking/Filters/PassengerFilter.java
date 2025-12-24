package com.zsgs.busbooking.Filters;

import com.zsgs.busbooking.util.JwtUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PassengerFilter implements Filter {


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setContentType("application/json");

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (JwtUtil.isTokenExpired(token)) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write(
                            "{\"error\": \"Token expired\", \"code\": \"TOKEN_EXPIRED\"}"
                    );
                    return;
                }

                String userType = JwtUtil.getRoleFromToken(token);

                // Check if user is passenger
                if (!"PASSENGER".equals(userType)) {
                    httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    httpResponse.getWriter().write(
                            "{\"error\": \"Access denied. Passenger access only.\", \"code\": \"WRONG_USER_TYPE\"}"
                    );
                    return;
                }

                // Set user info in request
                String userMobileNumber = JwtUtil.getMobileNumerFromToken(token);
                List<String> roles = JwtUtil.getRolesFromToken(token);

                httpRequest.setAttribute("username", userMobileNumber);
                httpRequest.setAttribute("userType", userType);
                httpRequest.setAttribute("roles", roles);

                chain.doFilter(request, response);

            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write(
                        "{\"error\": \"Invalid token\", \"code\": \"INVALID_TOKEN\"}"
                );
            }
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write(
                    "{\"error\": \"No token provided\", \"code\": \"NO_TOKEN\"}"
            );
        }
    }
}
