package com.zsgs.busbooking.Controllers;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.model.Trip;
import com.zsgs.busbooking.payloads.CreateTripRequest;
import com.zsgs.busbooking.payloads.TripDto;
import com.zsgs.busbooking.payloads.UpdatetripRequest;
import com.zsgs.busbooking.services.TripService;
import com.zsgs.busbooking.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class TripController extends HttpServlet {

    TripService tripService = AppContext.getInstance().getTripService();
    Gson gson = GsonUtil.getGson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        getCurrentTrips(req,resp);


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String path = req.getContextPath();

        if ( path == "update"){

            updateTrip(req,resp);

        } else if (path == "status"){

            updateStatus(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {

            CreateTripRequest tripRequest = gson.fromJson(req.getReader(), CreateTripRequest.class);


            Trip trip = tripService.createTrip(tripRequest);

            if (trip != null) {
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                resp.getWriter().write(gson.toJson("Trip created successfully"));
                resp.getWriter().write(gson.toJson(trip));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson("Failed to create trip"));
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error processing request: " + e.getMessage()));
        }
    }



    private void getCurrentTrips(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<TripDto> trips = tripService.getCurrentTrips();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(trips));

        } catch (SQLException e) {

            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error: Database connection failed"));

        } catch (IOException e) {

            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error: An unexpected error occurred"));
        }
    }

    public void updateTrip(HttpServletRequest req , HttpServletResponse resp)throws IOException {

        try{

            UpdatetripRequest updaterequest = gson.fromJson(req.getReader(),UpdatetripRequest.class);
            tripService.updateTrip(updaterequest);

            resp.setStatus(200);
            resp.getWriter().write(
                    "{ \" message \" : \" success \" }"
            );


        } catch (IOException e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(
                    "{\" message \" : \" error\"}"
            );

        } catch (SQLException e) {

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error: Database connection failed"));

        }
    }

    public void updateStatus(HttpServletRequest req ,HttpServletResponse resp)throws IOException {

        try {

            String tripId = req.getParameter("tripId");
            String status = req.getParameter("status");

             boolean update = tripService.updateTripStatus(tripId,status);

             if (update){

                 resp.setStatus(HttpServletResponse.SC_OK);
                 resp.getWriter().write(gson.toJson("Message : Success"));

             }

             resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
             resp.getWriter().write(gson.toJson("Error : Not Updated"));

        } catch (IOException e){

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error: An unexpected error occurred"));


        } catch (SQLException e){

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error:Database Not Connected "));

        }
    }


}
