package com.zsgs.busbooking.Controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.exception.InvalidRequest;
import com.zsgs.busbooking.payloads.AddBusRequest;
import com.zsgs.busbooking.services.BusService;
import com.zsgs.busbooking.util.GsonUtil;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;


public class BusController extends HttpServlet {

    BusService busService = AppContext.getInstance().getBusService();
    Gson gson = GsonUtil.getGson();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            AddBusRequest addRequest = gson.fromJson(req.getReader(), AddBusRequest.class);
            busService.addBus(addRequest);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(new ResponseMessage("Bus added successfully")));

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Database error: " + e.getMessage())));

        } catch (InvalidRequest e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));

        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid JSON format")));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String pathInfo = request.getPathInfo(); // Gets path after /api/bus

        try {

            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/all")) {
                getAllBuses(response);
            }

            else {
                String busNumber = pathInfo.substring(1); // Remove leading "/"
                getBusByNumber(response, busNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Database error: " + e.getMessage())));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Server error: " + e.getMessage())));
        }
    }

    private void getAllBuses(HttpServletResponse response) throws IOException, SQLException {
        var buses = busService.getAllBuses();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(buses));
    }

    private void getBusByNumber(HttpServletResponse response, String busNumber)
            throws IOException, SQLException {
        var bus = busService.getBusByNumber(busNumber);

        if (bus != null) {

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(bus));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(gson.toJson(new ErrorResponse("Bus not found with number: " + busNumber)));
        }
    }


    private static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }
    }

    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }


}