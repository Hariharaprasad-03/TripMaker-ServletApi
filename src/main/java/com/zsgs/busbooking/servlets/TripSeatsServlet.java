package com.zsgs.busbooking.servlets;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;

import com.zsgs.busbooking.payloads.TripSeatResponse;
import com.zsgs.busbooking.services.TripService;
import com.zsgs.busbooking.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class TripSeatsServlet  extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        TripService tripService = AppContext.getInstance().getTripService();
        Gson gson = GsonUtil.getGson();

        try {

            String tripId = req.getParameter("tripId");
            String busId = req.getParameter("busId");

            TripSeatResponse responseData =  tripService.getTripStatusData(tripId,busId);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(responseData));


        } catch (SQLException e){

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error : Database Not Connected "));
        }
    }
}
