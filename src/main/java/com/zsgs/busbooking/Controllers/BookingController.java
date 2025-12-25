package com.zsgs.busbooking.Controllers;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.payloads.BookingRequest;
import com.zsgs.busbooking.services.BookingServices;
import com.zsgs.busbooking.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookingController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        BookingServices bookingServices = AppContext.getInstance().getBookingServices();
        Gson gson = GsonUtil.getGson();

        try{

            BookingRequest bookingRequest = gson.fromJson(req.getReader(),BookingRequest.class);

            boolean booked  = bookingServices.BookTripWithAtomicity(bookingRequest);

            if (booked) {

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson("success : true"));
                resp.getWriter().write(gson.toJson("message: bookedsucessFully"));

            }

        } catch (SQLException e){


        }
    }
}
