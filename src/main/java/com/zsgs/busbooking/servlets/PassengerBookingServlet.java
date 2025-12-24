package com.zsgs.busbooking.servlets;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.payloads.UserBookingDto;
import com.zsgs.busbooking.services.BookingServices;
import com.zsgs.busbooking.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PassengerBookingServlet extends HttpServlet {

    BookingServices bookingServices = AppContext.getInstance().getBookingServices();
    Gson gson = GsonUtil.getGson();


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {

            String id  = req.getParameter("passengerID").trim();
            List<UserBookingDto> bookings = bookingServices.getPassengerBookings(id);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(bookings));

            } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error : Database Error ");

        }



    }
}
