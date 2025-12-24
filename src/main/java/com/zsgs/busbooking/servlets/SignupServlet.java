package com.zsgs.busbooking.servlets;

import com.google.gson.Gson;
import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.exception.DuplicateEntityException;
import com.zsgs.busbooking.model.Passenger;
import com.zsgs.busbooking.payloads.PassengerSignUpRequest;
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

public class SignupServlet extends HttpServlet {


    Gson gson = GsonUtil.getGson();
    PassengerService passengerService = AppContext.getInstance().getPassengerService();

    @Override
    public void init() throws ServletException {
        System.out.println("tomcat found me ");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");


        try {



            PassengerSignUpRequest signUpRequest = gson.fromJson(req.getReader(),PassengerSignUpRequest.class);

            if (signUpRequest.getPassengerName() == null || signUpRequest.getEmail() == null || signUpRequest.getMobileNumber() == null
            || signUpRequest.getPassword() == null) {

                Map<String,Object> response = new HashMap<>();
                response.put("sucesss" , false);
                response.put("message ", " Insufficent Date ");
                return ;
            }


            Passenger exist = passengerService.getPassengerByMobileNumber(signUpRequest.getMobileNumber());
            if (exist != null) {

                Map<String,Object> response = new HashMap<>();
                response.put("sucesss" , false);
                response.put("message ", "UserAlready Exist with this Mobile Number");


            }
            exist = passengerService.getPassengerByEmail(signUpRequest.getEmail());

            if( exist != null) {
                Map<String,Object> response = new HashMap<>();
                response.put("sucesss" , false);
                response.put("message ", "UserAlready Exist with this Email");

            }

            signUpRequest.setPassword(PasswordUtil.hashPassword(signUpRequest.getPassword()));
            Passenger added  = passengerService.addPassenger(signUpRequest);

            Map<String,Object> response = new HashMap<>();

            response.put("success",true);
            response.put("username",added.getPassengerName());

            String token = JwtUtil.generateToken(added.getMobileNumber(),"PASSENGER");
            response.put("token",token);
            response.put("role" ,"PASSENGER");
            response.put("message","Registration Successful");
            response.put("passengerId" , added.getPassengerId());

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(response));



        } catch (SQLException e) {

            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error : DatabaseNot Connected"));



        } catch (DuplicateEntityException e) {

            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson("Error : UserAlreay Exist" ));

        } catch (IOException e) {

            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson("Error : InternalServerError" ));
        }catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"Server error occurred\"}");
        }

    }
}
