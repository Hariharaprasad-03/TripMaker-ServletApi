package com.zsgs.busbooking.Filters;

import com.zsgs.busbooking.util.JwtUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PassengerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setContentType("application/json");


        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        System.out.println("In Passenger Auth Filter");

        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write(
                    "{\"error\": \"No token provided\", \"code\": \"NO_TOKEN\"}"
            );
            return;
        }

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

            if (!"PASSENGER".equals(userType)) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.getWriter().write(
                        "{\"error\": \"Access denied. Passenger access only\", \"code\": \"WRONG_USER_TYPE\"}"
                );
                return;
            }

            String mobile = JwtUtil.getMobileNumerFromToken(token);
            List<String> roles = JwtUtil.getRolesFromToken(token);

            httpRequest.setAttribute("usermobile", mobile);
            httpRequest.setAttribute("userType", userType);
            httpRequest.setAttribute("roles", roles);

            chain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write(
                    "{\"error\": \"Invalid token\", \"code\": \"INVALID_TOKEN\"}"
            );
        }
    }
}
