package com.zsgs.busbooking.Controllers;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.payloads.PassengerSignUpRequest;
import com.zsgs.busbooking.repositories.PassengerRepository;
import com.zsgs.busbooking.services.PassengerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/passenger")
public class UserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/add")) {

            addPassenger(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid POST endpoint\"}");
        }
    }



    public void addPassenger(HttpServletRequest req , HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PassengerService service = AppContext.getInstance().getPassengerService();
        Gson gson = new Gson();

        try{


            PassengerSignUpRequest request = gson.fromJson(req.getReader(),PassengerSignUpRequest.class);
            service.addPassenger(request);

        } catch (IOException e) {

            resp.setStatus(500);
            String message = "{ \" message \" : \" error \" , \"cause \" : " + "\"" + e.getMessage()+"\"" +"}";
            resp.getWriter().write(message);

        } catch (SQLException e) {

                resp.setStatus(400);
                resp.getWriter().write(gson.toJson("error : internal Server Error"));
        }
    }
}
