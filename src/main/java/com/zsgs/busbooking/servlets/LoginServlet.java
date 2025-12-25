package com.zsgs.busbooking.servlets;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.model.Passenger;
import com.zsgs.busbooking.services.PassengerService;
import com.zsgs.busbooking.util.GsonUtil;
import com.zsgs.busbooking.util.JwtUtil;
import com.zsgs.busbooking.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    public void init() throws ServletException {
        System.out.println("tomcat found me Login page  ");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Gson gson = GsonUtil.getGson();
        PassengerService passengerService = AppContext.getInstance().getPassengerService();

        try {

            String mobileNumber = (String) req.getParameter("mobileNumber");
            String password = (String) req.getParameter("password");



            Passenger passenger = passengerService.getPassengerByMobileNumber(mobileNumber);
            if (passenger == null){
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write(gson.toJson("message : mobile Number Not Registred"));
            }

            if ( PasswordUtil.verifyPassword(password,passenger.getPassword())){

                String token = JwtUtil.generateToken(passenger.getMobileNumber(), "PASSENGER");

                System.out.println(mobileNumber);
                System.out.println(token);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("token", token);
                responseData.put("username", passenger.getPassengerName());
                responseData.put("role", "PASSENGER");
                responseData.put("passengerId", passenger.getPassengerId());

                responseData.put("message", "Login successful");

                resp.getWriter().write(gson.toJson(responseData));

                System.out.println(passenger.getPassengerId());

            }
            else {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson("message : Database Error"));
        }
    }
}
