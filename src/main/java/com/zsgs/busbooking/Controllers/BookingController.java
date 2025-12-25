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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingController extends HttpServlet {

    BookingServices bookingServices = AppContext.getInstance().getBookingServices();
    Gson gson = GsonUtil.getGson();

    public void init(){
        System.out.println(" Boking is started");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try{

            BookingRequest bookingRequest = gson.fromJson(req.getReader(),BookingRequest.class);
            boolean booked  = bookingServices.BookTripWithAtomicity(bookingRequest);

            if (booked) {

                resp.setStatus(HttpServletResponse.SC_OK);
                Map<String, String> data = new HashMap<>();
                data.put("message", "bookedSuccessfully");
                data.put("status", "success");

                String json = new Gson().toJson(data);
                resp.getWriter().write(json);

            }

        } catch (SQLException e){


        }
    }
}
