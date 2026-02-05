package com.zsgs.busbooking.Controllers;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.payloads.PassengerDto;
import com.zsgs.busbooking.services.PassengerService;
import com.zsgs.busbooking.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class PassengerController extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Gson gson = GsonUtil.getGson();
        PassengerService passengerService = AppContext.getInstance().getPassengerService();

        try{

            String passengerId = req.getParameter("passengerId");
            PassengerDto dto = passengerService.getPassengerDtoById(passengerId);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(dto));

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error : Database Not Connected"));

        }

    }
}
